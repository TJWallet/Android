package com.tianji.blockchain.util.blake2b.security;

import com.tianji.blockchain.util.blake2b.Blake2b;

public class Blake2b32Digest extends AbstractDigest implements Cloneable {
    private static final int DIGEST_SIZE = 32;

    public Blake2b32Digest() {
        super(Blake2b.BLAKE2_B_32, new Blake2b(DIGEST_SIZE));
    }

    public Object clone() throws CloneNotSupportedException {
        final Blake2b32Digest clone = (Blake2b32Digest) super.clone();

        clone.instance = new Blake2b(instance);

        return clone;
    }


}
