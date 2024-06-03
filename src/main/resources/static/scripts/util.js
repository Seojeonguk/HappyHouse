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

function saveSelectOptions(selectId, storageKey) {
    const options = [];

    $(`#${selectId} option`).each(function () {
        const value = $(this).attr('value');
        const text = $(this).text();
        const legalCode = $(this).attr('legalCode');
        options.push({value, text, legalCode});
    });

    localStorage.setItem(storageKey, JSON.stringify(options));
}

function populateSelectOptions(selectId, storageKey, selectedValue) {
    const options = JSON.parse(localStorage.getItem(storageKey));
    if (options) {
        const select = $(`#${selectId}`);
        select.empty();
        options.forEach(option => {
            const optionElement = $('<option></option>').attr('value', option.value).attr('legalCode', option.legalCode).text(option.text);
            if (selectedValue && selectedValue === option.value) {
                optionElement.attr('selected', 'selected');
            }

            select.append(optionElement);
        });
        return true;
    }

    return false;
}

function createIcon(name, icon, parent, isAddText) {
    const div = $("<div></div>").addClass(name);
    $("<i></i>").attr('aria-hidden', 'true').attr('title', name)
        .addClass('fa').addClass('fa-icon').addClass(icon).addClass('fa-fw')
        .appendTo(div);
    $("<span></span>").addClass('sr-only').html(name).appendTo(div);
    if (isAddText) {
        $("<span></span>").html(name).appendTo(div);
    }

    div.appendTo(parent);

    return div;
}
