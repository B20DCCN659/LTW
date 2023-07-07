package com.example.btl_web.utils;


import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class BytePartUtils implements Part
{
    private final byte[] data;
    private final String name;
    private final String contentType;

    public BytePartUtils(byte[] data, String name) {
        this(data, name, null);
    }

    public BytePartUtils(byte[] data, String name, String contentType) {
        this.data = data;
        this.name = name;
        this.contentType = contentType != null ? contentType : "application/octet-stream";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSubmittedFileName() {
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public void write(String s) throws IOException {

    }

    @Override
    public void delete() throws IOException {

    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    // Các phương thức khác của interface Part có thể throw UnsupportedOperationException

}
