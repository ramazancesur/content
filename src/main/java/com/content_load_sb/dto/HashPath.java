package com.content_load_sb.dto;

public class HashPath extends Object {

    private String pathTail;
    private String hash;

    public HashPath(String pathTail, String hash) {
        this.pathTail = pathTail;
        this.hash = hash;
    }

    public HashPath() {
    }

    public String getPathTail() {
        return pathTail;
    }

    public void setPathTail(String pathTail) {
        this.pathTail = pathTail;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public int hashCode() {
        return this.hash == null ? 0 : this.hash.hashCode();
    }

    @Override
    public String toString() {
        return "Hash Path Code: " + this.hash;
    }
}
