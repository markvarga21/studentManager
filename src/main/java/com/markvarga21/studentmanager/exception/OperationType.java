package com.markvarga21.studentmanager.exception;

/**
 * A class for representing the operation which the user just did,
 * thus causing an exception to raise.
 */
public enum OperationType {
    /**
     * An operations which involves creating an object.
     */
    CREATE,
    /**
     * An operations which involves reading an object.
     */
    READ,
    /**
     * An operations which involves updating an object.
     */
    UPDATE,
    /**
     * An operations which involves deleting an object.
     */
    DELETE
}
