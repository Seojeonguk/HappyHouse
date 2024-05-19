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

function loadQueryParams(...args) {
    const params = new URLSearchParams(window.location.search);
    const queryParams = {};

    args.forEach(arg => {
        if (params.has(arg)) {
            queryParams[arg] = params.get(arg);
        }
    })

    return queryParams;
}