package ru.itmo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.itmo.gui.Wizard;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Администратор on 06.08.15.
 */
public class Test {
    public static final String DEFAULT_RESOURCE_PATH = "res/";
    public static final String BASE = "";

    public static void main(String[] args) {
        byte[] resp = new byte[]{0x00, 0x04, 0x32, 0x09, 0x15, 0x00, 0x06, 0x11, 0x15, 0x1E, 0x00, 0x00};
        //Wizard wizard = new Wizard();
        int lastAddr = (((resp[1] & 0xFF) << 8 | resp[2] & 0xFF) * 16);
        int ts1 = secondsFrom2000((byte) fromBCD(resp[8]),
                (byte) fromBCD(resp[7]), (byte) fromBCD(resp[6]),
                (byte) fromBCD(resp[4]), (byte) fromBCD(resp[5]),
                (byte) 0);
        int ts2 = secondsFrom2000((byte) 15, (byte) 10, (byte) 14, (byte) 22, (byte) 0, (byte) 0);
        int shift = -(ts1 - ts2) / 60 / resp[9];
        System.out.println(lastAddr);
        System.out.println(lastAddr/16);
        System.out.println(Integer.toHexString(48*170/2));
        System.out.println(shift);
        int b = calculateAddress(lastAddr, shift);
        System.out.println(b);
        System.out.println(Integer.toHexString(b));
    }

    public static int calculateAddress(int lastAddr, int shift) {
        int address = 0;
        if (shift >= 0) {
            if (lastAddr + shift * 16 > 0x1FFFF) {
                address = shift * 16 - (0x1FFFF - lastAddr);
            } else {
                address = lastAddr + shift * 16;
            }
        } else {
            if (lastAddr >= (-shift) * 16) {
                address = lastAddr + shift * 16;
            } else {
                address = 0x1FFFF - ((-shift) * 16 - lastAddr);
            }
        }
        return address;
    }

    public static int fromBCD(int a) {
        return ((a >> 4) * 10) + (a & 0x0f);
    }

    public static int calculateDaysFrom2000(byte year, byte month, byte day) {
        int counter = 0;

        // Add amount of days since 2000 to current year.
        for (int i = 0; i < year; i++) {
            if (isLeapYear(i + 2000)) {
                counter += 366;
            } else {
                counter += 365;
            }
        }

        boolean leapYear = isLeapYear(2000 + year); // for current year

        // Add amount of days in last months of the current year.
        for (byte i = 1; i < month; i++) {
            counter += getDayOfMonth(leapYear, i);
        }

        counter += day - 1; // Add amount of days in the current month.
        return counter;
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 & year % 100 != 0 | year % 400 == 0;
    }

    public static byte getDayOfMonth(boolean leapYear, byte month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;

            case 2:
                return (byte) (leapYear ? 29 : 28);

            case 4:
            case 6:
            case 9:
            case 11:
            default:
                break;
        }

        return 30;
    }

    public static int secondsFrom2000(byte year, byte month, byte day, byte hour, byte minute, byte second) {
        int counter = calculateDaysFrom2000(year, month, day);
        // convert to seconds
        counter *= (24 * 60 * 60);
        // add hours, minutes and seconds in in seconds
        counter += (hour * 60 * 60 + minute * 60 + second);
        return counter;
    }
}
