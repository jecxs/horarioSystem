/**
 * Script principal para el sistema de horarios
 */
$(document).ready(function() {
    // Inicializar tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Inicializar popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Validación de formularios
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // Resetear formularios al cerrar modales
    $('.modal').on('hidden.bs.modal', function () {
        const forms = $(this).find('form');
        forms.each(function() {
            this.reset();
            $(this).removeClass('was-validated');
        });
    });

    // Función para formatear fechas
    window.formatDateTime = function(dateTimeStr) {
        if (!dateTimeStr) return '';

        // Si solo es hora (HH:MM:SS)
        if (dateTimeStr.length <= 8) {
            return dateTimeStr.substring(0, 5);
        }

        // Si es fecha y hora
        const date = new Date(dateTimeStr);
        return date.toLocaleString();
    };

    // Función para formatear solo hora
    window.formatTime = function(timeStr) {
        if (!timeStr) return '';
        return timeStr.substring(0, 5);
    };

    // Función para obtener parámetros de URL
    window.getUrlParameter = function(name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        var results = regex.exec(location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    // Función para mostrar errores de validación
    window.showValidationErrors = function(form, errors) {
        // Limpiar errores anteriores
        form.find('.is-invalid').removeClass('is-invalid');
        form.find('.invalid-feedback').remove();

        // Mostrar nuevos errores
        for (const field in errors) {
            const input = form.find(`[name="${field}"]`);
            input.addClass('is-invalid');

            const feedback = $('<div>')
                .addClass('invalid-feedback')
                .text(errors[field]);

            input.after(feedback);
        }
    };

    // Función para mostrar mensajes de alerta
    window.showAlert = function(message, type = 'info') {
        const alertPlaceholder = document.getElementById('alertPlaceholder');
        if (!alertPlaceholder) return;

        const wrapper = document.createElement('div');
        wrapper.innerHTML = `
            <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;

        alertPlaceholder.append(wrapper);

        // Auto-cerrar después de 5 segundos
        setTimeout(() => {
            const alert = bootstrap.Alert.getOrCreateInstance(wrapper.querySelector('.alert'));
            alert.close();
        }, 5000);
    };

    // Confirmar acciones destructivas
    $('body').on('click', '[data-confirm]', function(e) {
        const message = $(this).data('confirm') || '¿Está seguro?';
        if (!confirm(message)) {
            e.preventDefault();
            e.stopImmediatePropagation();
            return false;
        }
    });

    // Animación para mostrar/ocultar elementos
    $('.fade-in').each(function() {
        $(this).addClass('show');
    });

    // Función para recargar una sección específica
    window.reloadSection = function(selector, url) {
        $.ajax({
            url: url,
            success: function(data) {
                $(selector).html(data);
            }
        });
    };

    // Hacer que los select múltiples sean más fáciles de usar
    $('.select-multiple').select2({
        width: '100%',
        language: 'es'
    });
});