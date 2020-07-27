package com.tianji.blockchain.util.blake2b;

/*
 *    Original implementation, Copyright (c) 2000-2017 The Legion of the Bouncy Castle Inc.
 *
 *    The BLAKE2 cryptographic hash function was designed by Jean-Philippe Aumasson,
 *    Samuel Neves, Zooko Wilcox-O'Hearn, and ChristianWinnerlein.
 *
 *    Reference Implementation and Description can be found at: https://blake2.net/
 *    Internet Draft: https://tools.ietf.org/html/draft-saarinen-blake2-02
 *
 *    This implementation does not support the Tree Hashing Mode.
 *
 *    For unkeyed hashing, developers adapting BLAKE2 to ASN.1 - based
 *    message formats SHOULD use the OID tree at x = 1.3.6.1.4.1.1722.12.2.
 *
 *         Algorithm     | Target | Collision | Hash | Hash ASN.1 |
 *            Identifier |  Arch  |  Security |  nn  | OID Suffix |
 *        ---------------+--------+-----------+------+------------+
 *         id-blake2b32 | 64-bit |   2**16   |  4  |   x.1.20   |
 *         id-blake2b160 | 64-bit |   2**80   |  20  |   x.1.20   |
 *         id-blake2b256 | 64-bit |   2**128  |  32  |   x.1.32   |
 *         id-blake2b384 | 64-bit |   2**192  |  48  |   x.1.48   |
 *         id-blake2b512 | 64-bit |   2**256  |  64  |   x.1.64   |
 *        ---------------+--------+-----------+------+------------+
 */


import static com.tianji.blockchain.util.blake2b.ArrayUtils.cloneByteArray;
import static com.tianji.blockchain.util.blake2b.ArrayUtils.fillByteArray;
import static com.tianji.blockchain.util.blake2b.Constants.BLAKE2B_IV;
import static java.lang.System.arraycopy;

/**
 * Blake2b offers a built-in keying mechanism to be used directly
 * for authentication ("Prefix-MAC") rather than a HMAC construction.
 * <p>
 * Blake2b offers a built-in support for a salt for randomized hashing
 * and a personal string for defining a unique hash function for each application.
 * <p>
 * BLAKE2b is optimized for 64-bit platforms and produces digests of any size
 * between 1 and 64 bytes.
 */
public class Blake2b {
    public static final String BLAKE2_B_32 = "BLAKE2B-32";
    public static final String BLAKE2_B_160 = "BLAKE2B-160";
    public static final String BLAKE2_B_256 = "BLAKE2B-256";
    public static final String BLAKE2_B_384 = "BLAKE2B-384";
    public static final String BLAKE2_B_512 = "BLAKE2B-512";
    // General parameters
    private int digestSize = 64; // 1- 64 bytes
    private int keyLength = 0; // 0 - 64 bytes for keyed hashing for MAC
    private byte[] salt = null;// new byte[16];
    private byte[] personalization = null;// new byte[16];

    // The key
    private byte[] key = null;

    // whenever this buffer overflows, it will be processed
    // in the compress() function.
    // For performance issues, long messages will not use this buffer.
    private byte[] buffer = null;// new byte[BLOCK_LENGTH_BYTES];

    // Position of last inserted byte:
    private int bufferPos = 0;// a value from 0 up to 128

    private final long[] internalState = new long[16]; // In the Blake2b paper it is called: v

    private long[] chainValue = null; // state vector, in the Blake2b paper it is called: h

    private long t0 = 0L; // holds last significant bits, counter (counts bytes)
    private long t1 = 0L; // counter: Length up to 2^128 are supported
    private long f0 = 0L; // finalization flag, for last block: ~0L

    public Blake2b() {
        this(512);
    }

    public Blake2b(final Blake2b digest) {
        this.bufferPos = digest.bufferPos;
        this.buffer = cloneByteArray(digest.buffer);
        this.keyLength = digest.keyLength;
        this.key = cloneByteArray(digest.key);
        this.digestSize = digest.digestSize;
        this.chainValue = cloneByteArray(digest.chainValue);
        this.personalization = cloneByteArray(digest.personalization);
        this.salt = cloneByteArray(digest.salt);
        this.t0 = digest.t0;
        this.t1 = digest.t1;
        this.f0 = digest.f0;
    }

    /**
     * Basic sized constructor - size in bits.
     *
     * @param digestSize size of the digest in bits
     */
    public Blake2b(final int digestSize) {
        if (digestSize != 32 && digestSize != 160 && digestSize != 256 && digestSize != 384 && digestSize != 512) {
            throw new IllegalArgumentException("Blake2b digest restricted to one of [160, 256, 384, 512]");
        }

        buffer = new byte[Constants.BLOCK_LENGTH_BYTES];
        keyLength = 0;
        this.digestSize = digestSize / 8;
        init();
    }

    /**
     * Blake2b for authentication ("Prefix-MAC mode").
     * After calling the digest() method, the key will
     * remain to be used for further computations of
     * this instance.
     * The key can be overwritten using the clearKey() method.
     *
     * @param key A key up to 64 bytes or null
     */
    public Blake2b(final byte[] key) {
        buffer = new byte[Constants.BLOCK_LENGTH_BYTES];
        if (null != key) {
            this.key = new byte[key.length];
            arraycopy(key, 0, this.key, 0, key.length);

            if (64 < key.length) {
                throw new IllegalArgumentException(
                        "Keys > 64 are not supported");
            }

            keyLength = key.length;
            arraycopy(key, 0, buffer, 0, key.length);
            bufferPos = Constants.BLOCK_LENGTH_BYTES; // zero padding
        }
        digestSize = 64;
        init();
    }

    /**
     * Blake2b with key, required digest length (in bytes), salt and personalization.
     * After calling the digest() method, the key, the salt and the personal string
     * will remain and might be used for further computations with this instance.
     * The key can be overwritten using the clearKey() method, the salt (pepper)
     * can be overwritten using the clearSalt() method.
     *
     * @param key             A key up to 64 bytes or null
     * @param digestSize      from 1 up to 64 bytes
     * @param salt            16 bytes or null
     * @param personalization 16 bytes or null
     */
    public Blake2b(final byte[] key, final int digestSize, final byte[] salt, final byte[] personalization) {
        buffer = new byte[Constants.BLOCK_LENGTH_BYTES];

        if (digestSize < 1 || digestSize > 64) {
            throw new IllegalArgumentException(
                    "Invalid digest length (required: 1 - 64)");
        }

        this.digestSize = digestSize;

        if (salt != null) {
            if (salt.length != 16) {
                throw new IllegalArgumentException(
                        "salt length must be exactly 16 bytes");
            }
            this.salt = new byte[16];
            arraycopy(salt, 0, this.salt, 0, salt.length);
        }

        if (personalization != null) {
            if (personalization.length != 16) {
                throw new IllegalArgumentException(
                        "personalization length must be exactly 16 bytes");
            }
            this.personalization = new byte[16];
            arraycopy(personalization, 0, this.personalization, 0,
                    personalization.length
            );
        }

        if (key != null) {
            this.key = new byte[key.length];
            arraycopy(key, 0, this.key, 0, key.length);

            if (key.length > 64) {
                throw new IllegalArgumentException(
                        "Keys > 64 are not supported");
            }
            keyLength = key.length;
            arraycopy(key, 0, buffer, 0, key.length);
            bufferPos = Constants.BLOCK_LENGTH_BYTES; // zero padding
        }

        init();
    }

    /**
     * update the message digest with a single byte.
     *
     * @param b the input byte to be entered.
     */
    public void update(final byte b) {
        // left bytes of buffer
        // process the buffer if full else add to buffer:
        final int remainingLength = Constants.BLOCK_LENGTH_BYTES - bufferPos;
        if (remainingLength == 0) { // full buffer
            t0 += Constants.BLOCK_LENGTH_BYTES;
            if (t0 == 0) { // if message > 2^64
                t1++;
            }
            compress(buffer, 0);
            fillByteArray(buffer, (byte) 0);// clear buffer
            buffer[0] = b;
            bufferPos = 1;
        } else {
            buffer[bufferPos] = b;
            bufferPos++;
        }
    }

    /**
     * update the message digest with a block of bytes.
     *
     * @param message the byte array containing the data.
     * @param offset  the offset into the byte array where the data starts.
     * @param len     the length of the data.
     */
    public void update(byte[] message, int offset, int len) {

        if (null == message || 0 == len) {
            return;
        }

        int remainingLength = 0; // left bytes of buffer

        if (0 != bufferPos) { // commenced, incomplete buffer

            // complete the buffer:
            remainingLength = Constants.BLOCK_LENGTH_BYTES - bufferPos;
            if (remainingLength < len) { // full buffer + at least 1 byte
                arraycopy(message, offset, buffer, bufferPos,
                        remainingLength
                );

                t0 += Constants.BLOCK_LENGTH_BYTES;

                if (0 == t0) { // if message > 2^64
                    t1++;
                }

                compress(buffer, 0);

                bufferPos = 0;

                fillByteArray(buffer, (byte) 0);// clear buffer
            } else {
                arraycopy(message, offset, buffer, bufferPos, len);

                bufferPos += len;

                return;
            }
        }

        // process blocks except last block (also if last block is full)
        int messagePos;
        final int blockWiseLastPos = offset + len - Constants.BLOCK_LENGTH_BYTES;

        // block wise 128 bytes
        for (messagePos = offset + remainingLength; messagePos < blockWiseLastPos; messagePos += Constants.BLOCK_LENGTH_BYTES) {
            // without buffer:
            t0 += Constants.BLOCK_LENGTH_BYTES;

            if (0 == t0) {
                t1++;
            }

            compress(message, messagePos);
        }

        // fill the buffer with left bytes, this might be a full block
        arraycopy(message, messagePos, buffer, 0, offset + len - messagePos);

        bufferPos += (offset + len) - messagePos;
    }

    /**
     * close the digest, producing the final digest value. The digest
     * call leaves the digest reset.
     * Key, salt and personal string remain.
     *
     * @param out       the array the digest is to be copied into.
     * @param outOffset the offset into the out array the digest is to start at.
     * @return length of the digest
     */
    public int digest(final byte[] out, final int outOffset) {

        f0 = Constants.F_0;
        t0 += bufferPos;

        if (0 < bufferPos && 0 == t0) {
            t1++;
        }

        compress(buffer, 0);
        fillByteArray(buffer, (byte) 0);// Holds eventually the key if input is null
        fillByteArray(internalState, 0L);

        for (int i = 0; i < chainValue.length && (i * 8 < digestSize); i++) {
            byte[] bytes = ByteUtils.long2bytes(chainValue[i]);

            if ((i * 8) < (digestSize - 8)) {
                arraycopy(bytes, 0, out, outOffset + (i * 8), 8);
            } else {
                arraycopy(bytes, 0, out, outOffset + (i * 8), digestSize - (i * 8));
            }
        }

        fillByteArray(chainValue, 0L);

        reset();

        return digestSize;
    }

    /**
     * Reset the digest back to it's initial state.
     * The key, the salt and the personal string will
     * remain for further computations.
     */
    public void reset() {
        bufferPos = 0;
        f0 = 0L;
        t0 = 0L;
        t1 = 0L;
        chainValue = null;

        fillByteArray(buffer, (byte) 0);

        if (key != null) {
            arraycopy(key, 0, buffer, 0, key.length);
            bufferPos = Constants.BLOCK_LENGTH_BYTES; // zero padding
        }

        init();
    }

    /**
     * return the size, in bytes, of the digest produced by this message digest.
     *
     * @return the size, in bytes, of the digest produced by this message digest.
     */
    public int getDigestSize() {
        return digestSize;
    }

    /**
     * Return the size in bytes of the internal buffer the digest applies it's compression
     * function to.
     *
     * @return byte length of the digests internal buffer.
     */
    public int getByteLength() {
        return Constants.BLOCK_LENGTH_BYTES;
    }

    /**
     * Overwrite the key
     * if it is no longer used (zeroization)
     */
    public void clearKey() {
        if (null != key) {
            fillByteArray(key, (byte) 0);
            fillByteArray(buffer, (byte) 0);
        }
    }

    /**
     * Overwrite the salt (pepper) if it
     * is secret and no longer used (zeroization)
     */
    public void clearSalt() {
        if (null != salt) {
            fillByteArray(salt, (byte) 0);
        }
    }

    // initialize chainValue
    private void init() {
        if (null == chainValue) {
            final long[] newChainValue = new long[8];

            newChainValue[0] = BLAKE2B_IV[0]
                    ^ (digestSize | (keyLength << 8) | 0x1010000);
            // 0x1010000 = ((fanout << 16) | (depth << 24) | (leafLength <<
            // 32));
            // with fanout = 1; depth = 0; leafLength = 0;
            newChainValue[1] = BLAKE2B_IV[1];// ^ nodeOffset; with nodeOffset = 0;
            newChainValue[2] = BLAKE2B_IV[2];// ^ ( nodeDepth | (innerHashLength <<
            // 8) );
            // with nodeDepth = 0; innerHashLength = 0;

            newChainValue[3] = BLAKE2B_IV[3];

            newChainValue[4] = BLAKE2B_IV[4];
            newChainValue[5] = BLAKE2B_IV[5];

            if (null != salt) {
                newChainValue[4] ^= (ByteUtils.bytes2long(salt, 0));
                newChainValue[5] ^= (ByteUtils.bytes2long(salt, 8));
            }

            newChainValue[6] = BLAKE2B_IV[6];
            newChainValue[7] = BLAKE2B_IV[7];

            if (null != personalization) {
                newChainValue[6] ^= (ByteUtils.bytes2long(personalization, 0));
                newChainValue[7] ^= (ByteUtils.bytes2long(personalization, 8));
            }

            chainValue = newChainValue;
        }
    }

    private void initializeInternalState() {
        // initialize v:
        arraycopy(chainValue, 0, internalState, 0, chainValue.length);
        arraycopy(BLAKE2B_IV, 0, internalState, chainValue.length, 4);

        internalState[12] = t0 ^ BLAKE2B_IV[4];
        internalState[13] = t1 ^ BLAKE2B_IV[5];
        internalState[14] = f0 ^ BLAKE2B_IV[6];
        internalState[15] = BLAKE2B_IV[7];// ^ f1 with f1 = 0
    }

    private void compress(byte[] message, int messagePos) {

        initializeInternalState();

        long[] m = new long[16];

        for (int j = 0; j < 16; j++) {
            m[j] = ByteUtils.bytes2long(message, messagePos + j * 8);
        }

        for (int round = 0; round < Constants.ROUNDS; round++) {
            // G apply to columns of internalState:m[BLAKE2B_SIGMA[round][2 * blockPos]] /+1
            G(m[Constants.BLAKE2B_SIGMA[round][0]], m[Constants.BLAKE2B_SIGMA[round][1]], 0, 4, 8,
                    12
            );
            G(m[Constants.BLAKE2B_SIGMA[round][2]], m[Constants.BLAKE2B_SIGMA[round][3]], 1, 5, 9,
                    13
            );
            G(m[Constants.BLAKE2B_SIGMA[round][4]], m[Constants.BLAKE2B_SIGMA[round][5]], 2, 6, 10,
                    14
            );
            G(m[Constants.BLAKE2B_SIGMA[round][6]], m[Constants.BLAKE2B_SIGMA[round][7]], 3, 7, 11,
                    15
            );
            // G apply to diagonals of internalState:
            G(m[Constants.BLAKE2B_SIGMA[round][8]], m[Constants.BLAKE2B_SIGMA[round][9]], 0, 5, 10,
                    15
            );
            G(m[Constants.BLAKE2B_SIGMA[round][10]], m[Constants.BLAKE2B_SIGMA[round][11]], 1, 6,
                    11, 12
            );
            G(m[Constants.BLAKE2B_SIGMA[round][12]], m[Constants.BLAKE2B_SIGMA[round][13]], 2, 7,
                    8, 13
            );
            G(m[Constants.BLAKE2B_SIGMA[round][14]], m[Constants.BLAKE2B_SIGMA[round][15]], 3, 4,
                    9, 14
            );
        }

        // update chain values:
        for (int offset = 0; offset < chainValue.length; offset++) {
            chainValue[offset] = chainValue[offset] ^ internalState[offset] ^ internalState[offset + 8];
        }
    }

    private void G(long m1, long m2, int posA, int posB, int posC, int posD) {
        internalState[posA] = internalState[posA] + internalState[posB] + m1;
        internalState[posD] = ByteUtils.rotr64(
                internalState[posD] ^ internalState[posA],
                32
        );

        internalState[posC] = internalState[posC] + internalState[posD];
        internalState[posB] = ByteUtils.rotr64(
                internalState[posB] ^ internalState[posC],
                24
        ); // replaces 25 of BLAKE

        internalState[posA] = internalState[posA] + internalState[posB] + m2;
        internalState[posD] = ByteUtils.rotr64(
                internalState[posD] ^ internalState[posA],
                16
        );

        internalState[posC] = internalState[posC] + internalState[posD];
        internalState[posB] = ByteUtils.rotr64(
                internalState[posB] ^ internalState[posC],
                63
        ); // replaces 11 of BLAKE
    }

}
