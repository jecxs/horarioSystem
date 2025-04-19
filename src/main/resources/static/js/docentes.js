$(document).ready(function() {
    // Inicializar DataTable (verificando primero si la función existe)
    let tablaDocentes;
    if ($.fn.DataTable) {
        tablaDocentes = $("#tablaDocentes").DataTable({
            language: {
                url: "//cdn.datatables.net/plug-ins/1.13.4/i18n/es-ES.json"
            },
            pageLength: 10,
            responsive: true,
            order: [[0, 'asc']]
        });

        // Buscar docentes con DataTables
        $("#btnBuscar").click(function() {
            const termino = $("#buscarDocente").val();
            tablaDocentes.search(termino).draw();
        });
    } else {
        // Implementación alternativa si DataTables no está disponible
        console.warn("DataTables no está disponible, usando búsqueda básica");

        $("#btnBuscar").click(function() {
            const termino = $("#buscarDocente").val().toLowerCase();
            $("#tablaDocentes tbody tr").each(function() {
                const texto = $(this).text().toLowerCase();
                $(this).toggle(texto.indexOf(termino) > -1);
            });
        });
    }

    // También buscar al presionar Enter
    $("#buscarDocente").keypress(function(e) {
        if (e.which === 13) {
            $("#btnBuscar").click();
        }
    });

    // Editar docente - Usando delegación de eventos para garantizar que funcione
    $(document).on("click", ".btn-editar-docente", function() {
        const id = $(this).data("id");
        const nombre = $(this).data("nombre");
        const especialidad = $(this).data("especialidad");

        $("#tituloModalDocente").text("Editar Docente");
        $("#docenteId").val(id);
        $("#nombreCompleto").val(nombre);
        $("#especialidad").val(especialidad);

        $("#modalDocente").modal("show");
    });

    // Gestionar disponibilidad - Usando delegación de eventos
    $(document).on("click", ".btn-disponibilidad", function() {
        const id = $(this).data("id");
        const nombre = $(this).data("nombre");

        $("#nombreDocenteDisp").text(nombre);
        $("#dispDocenteId").val(id);

        cargarDisponibilidadDocente(id);
        $("#modalDisponibilidad").modal("show");
    });

    // Eliminar docente - Usando delegación de eventos
    $(document).on("click", ".btn-eliminar-docente", function() {
        const id = $(this).data("id");
        if (confirm("¿Está seguro de eliminar este docente?")) {
            eliminarDocente(id);
        }
    });

    // Guardar docente (nuevo o edición)
    $("#btnGuardarDocente").click(function() {
        const id = $("#docenteId").val();
        const nombreCompleto = $("#nombreCompleto").val();
        const especialidad = $("#especialidad").val();

        if (!nombreCompleto) {
            alert("El nombre del docente es obligatorio");
            return;
        }

        // Mostrar indicador de carga
        const btnGuardar = $(this);
        const textoOriginal = btnGuardar.text();
        btnGuardar.html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Guardando...');
        btnGuardar.prop('disabled', true);

        if (id) {
            // Actualizar docente existente
            // Primero, obtenemos los datos actuales del docente para preservar las disponibilidades
            $.ajax({
                url: `/docentes/${id}`,
                method: "GET",
                success: function(docenteActual) {
                    // Crear DTO con las disponibilidades actuales
                    const docenteDTO = {
                        nombreCompleto: nombreCompleto,
                        especialidad: especialidad,
                        disponibilidades: docenteActual.disponibilidades || [] // Mantener las disponibilidades existentes
                    };

                    // Ahora actualizamos el docente
                    $.ajax({
                        url: `/docentes/${id}`,
                        method: "PUT",
                        contentType: "application/json",
                        data: JSON.stringify(docenteDTO),
                        success: function() {
                            window.location.reload();
                        },
                        error: function(xhr) {
                            btnGuardar.text(textoOriginal);
                            btnGuardar.prop('disabled', false);
                            alert("Error al actualizar el docente: " + (xhr.responseText || "Verifique su conexión"));
                        }
                    });
                },
                error: function(xhr) {
                    btnGuardar.text(textoOriginal);
                    btnGuardar.prop('disabled', false);
                    alert("Error al obtener los datos actuales del docente: " + (xhr.responseText || "Verifique su conexión"));
                }
            });
        } else {
            // Crear nuevo docente (sin cambios, ya que un nuevo docente no tiene disponibilidades)
            const docenteDTO = {
                nombreCompleto: nombreCompleto,
                especialidad: especialidad,
                disponibilidades: []
            };

            $.ajax({
                url: "/docentes",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(docenteDTO),
                success: function() {
                    window.location.reload();
                },
                error: function(xhr) {
                    btnGuardar.text(textoOriginal);
                    btnGuardar.prop('disabled', false);
                    alert("Error al crear el docente: " + (xhr.responseText || "Verifique su conexión"));
                }
            });
        }
    });

    // Funciones para gestión de disponibilidad
    function cargarDisponibilidadDocente(docenteId) {
        // Mostrar indicador de carga
        const tbody = $("#tablaDisponibilidad tbody");
        tbody.html('<tr><td colspan="4" class="text-center"><div class="spinner-border spinner-border-sm" role="status"></div> Cargando...</td></tr>');

        $.ajax({
            url: `/docentes/${docenteId}`,
            method: "GET",
            success: function(docente) {
                tbody.empty();

                if (!docente.disponibilidades || docente.disponibilidades.length === 0) {
                    tbody.append(`<tr><td colspan="4" class="text-center">No hay disponibilidades registradas</td></tr>`);
                    return;
                }

                docente.disponibilidades.forEach(disp => {
                    const tr = $("<tr>");
                    tr.append(`<td>${disp.diaSemana}</td>`);
                    tr.append(`<td>${disp.horaInicio.substring(0, 5)}</td>`);
                    tr.append(`<td>${disp.horaFin.substring(0, 5)}</td>`);
                    tr.append(`
                            <td>
                                <button class="btn btn-sm btn-danger btn-eliminar-disponibilidad" data-id="${disp.id}">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        `);
                    tbody.append(tr);
                });
            },
            error: function(xhr) {
                tbody.html(`<tr><td colspan="4" class="text-center text-danger">Error al cargar los datos: ${xhr.responseText || "Verifique su conexión"}</td></tr>`);
            }
        });
    }
    // Editar docente - Usando delegación de eventos para garantizar que funcione
    $(document).on("click", ".btn-editar-docente", function() {
        const id = $(this).data("id");

        // Mostrar indicador de carga
        $("#modalDocente .modal-body").prepend('<div id="cargando-docente" class="alert alert-info"><span class="spinner-border spinner-border-sm" role="status"></span> Cargando datos del docente...</div>');

        // Deshabilitar campos mientras se cargan los datos
        $("#formDocente :input").prop("disabled", true);

        // Cargar datos actualizados del docente desde el servidor
        $.ajax({
            url: `/docentes/${id}`,
            method: "GET",
            success: function(docente) {
                // Actualizar el título y los campos
                $("#tituloModalDocente").text("Editar Docente");
                $("#docenteId").val(docente.idDocente);
                $("#nombreCompleto").val(docente.nombreCompleto);
                $("#especialidad").val(docente.especialidad || "");

                // Guardar las disponibilidades en una variable global para usar al guardar
                window.disponibilidadesDocente = docente.disponibilidades || [];

                // Quitar indicador de carga y habilitar campos
                $("#cargando-docente").remove();
                $("#formDocente :input").prop("disabled", false);
            },
            error: function(xhr) {
                // En caso de error, usar los datos del botón como fallback
                $("#tituloModalDocente").text("Editar Docente");
                $("#docenteId").val(id);
                $("#nombreCompleto").val($(this).data("nombre"));
                $("#especialidad").val($(this).data("especialidad") || "");

                // Mostrar mensaje de advertencia
                $("#cargando-docente").removeClass("alert-info").addClass("alert-warning")
                    .html('<i class="bi bi-exclamation-triangle me-2"></i> No se pudieron cargar todos los datos del docente. Algunas opciones pueden estar limitadas.');

                // Habilitar campos
                $("#formDocente :input").prop("disabled", false);
            }
        });

        $("#modalDocente").modal("show");
    });

    // Agregar disponibilidad
    $("#btnAgregarDisponibilidad").click(function() {
        const docenteId = $("#dispDocenteId").val();
        const dia = $("#diaSemana").val();
        const horaInicio = $("#horaInicio").val();
        const horaFin = $("#horaFin").val();

        if (!dia || !horaInicio || !horaFin) {
            alert("Todos los campos son obligatorios");
            return;
        }

        if (horaInicio >= horaFin) {
            alert("La hora de inicio debe ser menor a la hora de fin");
            return;
        }

        // Mostrar indicador de carga
        const btnAgregar = $(this);
        const textoOriginal = btnAgregar.text();
        btnAgregar.html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Agregando...');
        btnAgregar.prop('disabled', true);

        $.ajax({
            url: `/docentes/${docenteId}/disponibilidad`,
            method: "POST",
            data: {
                diaSemana: dia,
                horaInicio: horaInicio,
                horaFin: horaFin
            },
            success: function() {
                // Limpiar formulario
                $("#diaSemana").val("");
                $("#horaInicio").val("");
                $("#horaFin").val("");

                // Recargar tabla
                cargarDisponibilidadDocente(docenteId);

                // Restaurar botón
                btnAgregar.text(textoOriginal);
                btnAgregar.prop('disabled', false);
            },
            error: function(xhr) {
                btnAgregar.text(textoOriginal);
                btnAgregar.prop('disabled', false);
                alert("Error al agregar disponibilidad: " + (xhr.responseText || "Verifique su conexión"));
            }
        });
    });

    // Eliminar disponibilidad - usando delegación de eventos
    $(document).on("click", ".btn-eliminar-disponibilidad", function() {
        const dispId = $(this).data("id");
        const btn = $(this);

        if (confirm("¿Está seguro de eliminar esta disponibilidad?")) {
            // Deshabilitar botón y mostrar indicador
            btn.html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>');
            btn.prop('disabled', true);

            eliminarDisponibilidad(dispId);
        }
    });

    function eliminarDisponibilidad(id) {
        $.ajax({
            url: `/docentes/disponibilidad/${id}`,
            method: "DELETE",
            success: function() {
                const docenteId = $("#dispDocenteId").val();
                cargarDisponibilidadDocente(docenteId);
            },
            error: function(xhr) {
                alert("Error al eliminar la disponibilidad: " + (xhr.responseText || "Verifique su conexión"));

                // Restaurar botones de eliminación
                $(`.btn-eliminar-disponibilidad[data-id="${id}"]`).html('<i class="bi bi-trash"></i>').prop('disabled', false);
            }
        });
    }

    function eliminarDocente(id) {
        // Deshabilitar botón y mostrar indicador
        const btn = $(`.btn-eliminar-docente[data-id="${id}"]`);
        btn.html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>');
        btn.prop('disabled', true);

        $.ajax({
            url: `/docentes/${id}`,
            method: "DELETE",
            success: function() {
                // Mostrar mensaje de éxito y recargar
                alert("Docente eliminado con éxito");
                window.location.reload();
            },
            error: function(xhr) {
                // Restaurar botón
                btn.html('<i class="bi bi-trash"></i>');
                btn.prop('disabled', false);

                alert("Error al eliminar el docente: " + (xhr.responseText || "Verifique que no tenga asignaciones de horario."));
            }
        });
    }

});