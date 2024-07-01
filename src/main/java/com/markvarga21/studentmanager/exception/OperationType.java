package com.markvarga21.studentmanager.exception;

import com.markvarga21.studentmanager.util.Generated;

/**
 * A class for representing the operation which the user just did,
 * thus causing an exception to raise.
 */
@Generated
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
