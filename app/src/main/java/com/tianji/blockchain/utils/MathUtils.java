package com.tianji.blockchain.utils;

import com.tianji.blockchain.Constant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtils {

    public static double doubleKeep4(Double d, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        d = bigDecimal.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
        return d;
    }

    public static String doubleKeep4(Double d) {
        DecimalFormat df = new DecimalFormat("#0.0000");
        df.setRoundingMode(RoundingMode.DOWN);
        String str = df.format(d);
        return str;
    }

    public static String doubleKeep8(Double d) {
        DecimalFormat df = new DecimalFormat("#0.00000000");
        df.setRoundingMode(RoundingMode.DOWN);
        String str = df.format(d);
        return str;
    }

    public static String doubleKeep2(Double d) {
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        String str = df.format(d);
        return str;
    }


    public static String doubleKeep10(Double d) {
        DecimalFormat df = new DecimalFormat("#0.0000000000");
        df.setRoundingMode(RoundingMode.DOWN);
        String str = df.format(d);
        return str;
    }

    /**
     * @param gasPrice
     * @param speed    0 慢，1中，2快
     * @return
     */
    public static String getMinersFee(BigDecimal gasPrice, BigDecimal gasLimit, int speed) {
        BigDecimal price;
        switch (speed) {
            case 0:
                price = gasPrice.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_SLOW)).divide(new BigDecimal(Math.pow(10, 18))).multiply(gasLimit);
                break;
            case 1:
                price = gasPrice.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_NORMAL)).divide(new BigDecimal(Math.pow(10, 18))).multiply(gasLimit);
                break;
            case 2:
                price = gasPrice.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_FAST)).divide(new BigDecimal(Math.pow(10, 18))).multiply(gasLimit);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + speed);
        }
        return doubleKeep10(price.setScale(10, RoundingMode.DOWN).doubleValue());
    }

    public static String getETHUsedFee(String gasPrice, String gasLimit) {
        BigDecimal fee = new BigDecimal(gasPrice).multiply(new BigDecimal(gasLimit).divide(new BigDecimal(Math.pow(10, 18))));
        return doubleKeep10(fee.doubleValue());
    }

    public static BigDecimal getMinersFeeBigDecimal(BigDecimal gasPrice, BigDecimal gasLimit, int speed) {
        BigDecimal price;
        switch (speed) {
            case 0:
                price = gasPrice.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_SLOW)).divide(new BigDecimal(Math.pow(10, 9))).multiply(gasLimit);
                break;
            case 1:
                price = gasPrice.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_NORMAL)).divide(new BigDecimal(Math.pow(10, 9))).multiply(gasLimit);
                break;
            case 2:
                price = gasPrice.multiply(new BigDecimal(Constant.GasPriceSpeed.TYPE_SPEED_FAST)).divide(new BigDecimal(Math.pow(10, 9))).multiply(gasLimit);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + speed);
        }
        return price;
    }

}
