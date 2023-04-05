package com.ifpb.lattesmaismais.presentation.exception;

import java.io.Serial;

public class FileWithoutNameException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public FileWithoutNameException (String msg) {
        super(msg);
    }
}
