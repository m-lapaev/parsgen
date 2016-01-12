package ru.itmo.model;

public enum SelectedOption {
    BY_AT_VALUE_STARTING("By attribute value starting with ...", 0),
    BY_AT_VALUE_ENDING("By attribute value ending with ...", 1),
    BY_AT_VALUE_CONTAINING("By attribute value containing ...", 2),
    BY_CLASS_STARTING("By class starting with ...", 3),
    BY_TAG("By tag ...", 4),
    BY_ID_VALUE("By id value ...", 5),
    BY_NAME_VALUE("By name value ...", 6),
    BY_STYLE_CONTAINING("By style value containing ...", 7),
    WITHIN_INTERVAL("Walk around within intervalw ...", 8),
    WITHIN_THE_LIST("Walk around within the list ...", 9),
    WITHIN_CONTENT("Walk around by content within teh page ...", 10),
    NONE("", -1);
    String value;
    int index;

    SelectedOption(String value, int index) {
        this.value = value;
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }
}