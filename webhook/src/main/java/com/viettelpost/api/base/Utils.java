package com.viettelpost.api.base;

import com.viettelpost.api.business.models.PhoneData;
import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    public static void main(String[] args) throws Exception {
//        List<Integer> list = Arrays.asList(1, 2, 3, 4);
//        Optional<Integer> test = list.parallelStream().filter(x -> x > 2).findFirst();
//        if (test.isPresent()) {
//            System.out.println(test.get());
//        } else {
//            System.out.println("'232");
//        }
//        Long l1 = 2l;
//        System.out.println(l1.compareTo(3l));
        System.out.println(isNumber("123456l"));
        System.out.println(weightConvert("VBS", 1l, 15l, 20l));

        System.out.println(validatePhone("0844569885"));

        System.out.println(listPhone.contains(new PhoneData("0162", null)));
        System.out.println(listPhone.contains("0162"));
    }

    static List<PhoneData> listPhone = Arrays.asList(
            new PhoneData("0162", "032"),
            new PhoneData("0163", "033"),
            new PhoneData("0164", "034"),
            new PhoneData("0165", "035"),
            new PhoneData("0166", "036"),
            new PhoneData("0167", "037"),
            new PhoneData("0168", "038"),
            new PhoneData("0169", "039"),

            new PhoneData("0120", "070"),
            new PhoneData("0121", "079"),
            new PhoneData("0122", "077"),
            new PhoneData("0126", "076"),
            new PhoneData("0128", "078"),

            new PhoneData("0123", "083"),
            new PhoneData("0124", "084"),
            new PhoneData("0125", "085"),
            new PhoneData("0127", "081"),
            new PhoneData("0129", "082"),

            new PhoneData("0186", "056"),
            new PhoneData("0188", "058"),

            new PhoneData("0199", "059")
    );

    public static String getISDN(String phone) {
        phone = phone.trim();
        if (phone.length() > 10 || phone.length() < 9){
            return "";
        }
        if (phone.startsWith("00")) {
            return "";
        }
        if (phone.length() == 9 && phone.startsWith("0")){
            return "";
        }
        if (!phone.startsWith("0") && phone.length() == 10) {
            return "";
        } else if (phone.length() == 10){
            phone = phone.substring(1);
        }
        return phone;
    }

    public static String validatePhone(String phone) {
        phone = phone.trim();
        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }
        if (phone.startsWith("84") && phone.length() > 9) {
            phone = phone.substring(2);
        }
        if (phone.startsWith("000")) {
            phone = phone.substring(3);
        }
        if (phone.startsWith("00")) {
            phone = phone.substring(2);
        }
        if (phone.startsWith("0")) {
            phone = phone.substring(1);
        }
        if (phone.length() > 10) {
            return null;
        }
        if ((phone.startsWith("3") || phone.startsWith("5") || phone.startsWith("7") || phone.startsWith("8") || phone.startsWith("9")) && phone.length() != 9) {
            return null;
        }
        if (StringUtils.isNumeric(phone)) {
            if (phone.length() < 9) {
                return phone;
            }
            return "0" + phone;
        }
        return null;
    }

    public static boolean isNullOrEmpty(Object input) {
        if (input instanceof String) {
            return input == null || ((String) input).trim().isEmpty();
        }

        if (input instanceof List) {
            return input == null || ((List) input).isEmpty();
        }
        return input == null;
    }

    public static boolean isNumber(String input) {
        try {
            Long.valueOf(input);
            return true;
        } catch (Exception e) {
            //ignored
        }
        return false;
    }

    public static String convertNoUnicode(String str) {
        try {
            str = str.trim();
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "").replaceAll("đ", "d");
        } catch (Exception e) {
            //ignored
        }
        return "";
    }

    public static String convertNoUnicodeNormal(String str) {
        try {
            str = str.trim();
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replaceAll("\u0111", "d").replaceAll("\u0110", "d");
        } catch (Exception e) {
            //ignored
        }
        return "";
    }

    public static boolean compareDate(Date from, Date toDate, long time) {
        return from.getTime() - toDate.getTime() > time;
    }

    public static Long weightConvert(String serviceCode, Long length, Long width, Long height) {
        Long weight = 0l;
        if (length == null) {
            length = 0l;
        }
        if (width == null) {
            width = 0l;
        }
        if (height == null) {
            height = 0l;
        }
        if (height.compareTo(0l) < 0 || height.compareTo(0l) < 0 || height.compareTo(0l) < 0) {
            if (serviceCode.equals("VBE") || serviceCode.equals("VBE")) {
                weight = -1l;
            } else {
                weight = 0l;
            }
        } else {
            switch (serviceCode) {
                case "SCOD":
                    if ((length > 80) && (width > 80) && (height > 80)) {
                        weight = (length * width * height) * 1000 / 6000;
                    }
                    break;
                case "PTN":
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                case "PHT":
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                case "PHS":
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                case "VCN":
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                case "VHT":
                    weight = (length * width * height) * 1000 / 6000;
                    break;
                case "VTK":
                    weight = (length * width * height) * 1000 / 4000;
                    break;
                case "V60":
                    weight = (length * width * height) * 1000 / 4500;
                    break;
                case "VBE":
                    weight = ortherWeightConvert(length, width, height);
                    if (weight.compareTo(0l) == 0) {
                        weight = -1l;
                    }
                    break;
                case "VBS":
                    weight = ortherWeightConvert(length, width, height);
                    if (weight.compareTo(0l) == 0) {
                        weight = -1l;
                    }
                    break;
                default: {
                    break;
                }
            }
        }
        return weight.compareTo(0l) < 0 ? -1l : weight;
    }

    static Long ortherWeightConvert(Long length, Long width, Long height) {
        Long result = 0l;
        Long total = length + width + height;
        if (length.compareTo(0l) < 0 || width.compareTo(0l) < 0 || height.compareTo(0l) < 0) {
            return 0l;
        }
        if (length.compareTo(0l) == 0 || width.compareTo(0l) == 0 || height.compareTo(0l) == 0) {
            if (total.compareTo(40l) <= 0) {
                result = 100l;
            } else if (total.compareTo(60l) <= 0) {
                result = 500l;
            }
        } else {
            if (total.compareTo(15l) <= 0) {
                result = 100l;
            } else if (total.compareTo(30l) <= 0) {
                result = 500l;
            } else if (total.compareTo(40l) <= 0) {
                result = 1000l;
            } else {
                if (total.compareTo(60l) <= 0) {
                    result = 2000l;
                } else if (total.compareTo(80l) <= 0) {
                    result = 4000l;
                } else if (total.compareTo(100l) <= 0) {
                    result = 7000l;
                } else if (total.compareTo(120l) <= 0) {
                    result = 15000l;
                } else if (total.compareTo(140l) <= 0) {
                    result = 20000l;
                } else if (total.compareTo(160l) <= 0) {
                    result = 25000l;
                }
            }
        }
        return result;
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());

            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
