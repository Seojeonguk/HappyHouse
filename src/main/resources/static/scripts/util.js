function isEmpty(value) {
    if (value === null || value === undefined) {
        return true;
    }

    if (typeof value === 'string') {
        return value.trim() === '';
    }

    if (Array.isArray(value) || typeof value === 'object') {
        return Object.keys(value).length === 0;
    }

    return false;
}

/**
 * Verify that all inputs are nonempty.
 * @param args
 * @returns {boolean}
 */
function isVerifyInputFields(...args) {
    args.forEach(arg => {
        if (isEmpty(arg)) {
            return false;
        }
    });

    return true;
}