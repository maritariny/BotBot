package ru.maritariny.service.enums;

public enum LinkType {
    GET_DOC("file/get-doc"),
    GET_PHOTO("file/get-photo");
    private final String link;

    LinkType(String link) {
        this.link = link;
    }

    // Если не переопределить, то вернется то, что написано большими буквами, а не часть ссылки
    @Override
    public String toString() {
        return link;
    }
}
