// Objeto global para compartir funciones y estado
window.HorarioApp = {
    docenteSeleccionado: null,
    horarioData: null,

    // Función para inicializar la aplicación
    init: function() {
        // Inicializar eventos comunes
        $("#btnVerHorario").click(function() {
            const docenteId = $("#selectDocente").val();
            if (!docenteId) {
                alert("Por favor seleccione un docente");
                return;
            }

            HorarioApp.cargarHorarioDocente(docenteId);
        });

        // Inicializar los demás módulos
        HorarioApp.Docente.init();
        HorarioApp.Disponibilidad.init();
        HorarioApp.Asignacion.init();
        HorarioApp.Ambiente.init();
        HorarioApp.Configuracion.init();
    },

    // Función para cargar el horario del docente
    cargarHorarioDocente: function(docenteId) {
        $.ajax({
            url: `/horarios/docente/${docenteId}`,
            method: "GET",
            success: function(data) {
                HorarioApp.docenteSeleccionado = data.docente;
                HorarioApp.horarioData = data;
                HorarioApp.mostrarInfoDocente();
                HorarioApp.construirTablaHorario();

                $("#btnNuevaAsignacion").show();
            },
            error: function() {
                alert("Error al cargar el horario del docente");
            }
        });
    },

    // Función para mostrar info del docente
    mostrarInfoDocente: function() {
        $("#nombreDocente").text(HorarioApp.docenteSeleccionado.nombreCompleto);
        $("#especialidadDocente").text(HorarioApp.docenteSeleccionado.especialidad || "Sin especialidad");
        $("#infoDocente").show();
        $("#asignacionDocenteId").val(HorarioApp.docenteSeleccionado.idDocente);
        HorarioApp.Disponibilidad.cargarDisponibilidadDocente();
    },

    // Función para construir la tabla de horario
    construirTablaHorario: function() {
        // Obtener todos los bloques para construir la tabla
        $.ajax({
            url: "/ambientes/bloques",
            method: "GET",
            success: function(bloques) {
                const tbody = $("#tablaHorario tbody");
                tbody.empty();

                // Ordenar bloques por hora de inicio
                bloques.sort((a, b) => {
                    return a.horaInicio.localeCompare(b.horaInicio);
                });

                // Crear filas para cada bloque
                bloques.forEach(bloque => {
                    const tr = $("<tr>");

                    // Celda del bloque (primera columna)
                    tr.append(`<td class="fw-bold">${bloque.horaInicio.substring(0, 5)} - ${bloque.horaFin.substring(0, 5)}</td>`);

                    // Celdas para cada día de la semana
                    for (const dia of ["Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"]) {
                        // Buscar disponibilidad para este bloque en este día
                        const disponibleInfo = HorarioApp.horarioData.disponibilidadPorDia[dia].find(b => b.bloqueId === bloque.idBloque);

                        // Buscar asignaciones para este bloque en este día
                        const asignaciones = HorarioApp.horarioData.asignacionesPorDia[dia].filter(a =>
                            a.bloques.some(b => b.idBloque === bloque.idBloque)
                        );

                        const td = $("<td>");

                        if (asignaciones && asignaciones.length > 0) {
                            // Hay una asignación para este bloque
                            td.addClass("bloque-asignado");
                            const asignacion = asignaciones[0];

                            td.append(`
                            <div class="asignacion-item" data-id="${asignacion.id}">
                                <strong>${asignacion.curso.nombre}</strong><br>
                                <small>${asignacion.grupo.nombre} | ${asignacion.ambiente.nombre}</small>
                                <button class="btn btn-sm btn-danger float-end btn-borrar-asignacion"
                                    data-id="${asignacion.id}" data-bs-toggle="tooltip" title="Eliminar">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </div>
                        `);

                            // Evento para eliminar asignación
                            td.find(".btn-borrar-asignacion").click(function(e) {
                                e.stopPropagation();
                                const asignacionId = $(this).data("id");
                                if (confirm("¿Está seguro de eliminar esta asignación?")) {
                                    HorarioApp.Asignacion.eliminarAsignacion(asignacionId);
                                }
                            });
                        } else if (disponibleInfo && disponibleInfo.disponible) {
                            // El bloque está disponible
                            td.addClass("bloque-disponible");
                            td.attr("data-dia", dia);
                            td.attr("data-bloque", bloque.idBloque);

                            // IMPORTANTE: Aquí modificamos el evento para usar el objeto global
                            td.click(function() {
                                HorarioApp.Asignacion.iniciarNuevaAsignacion(dia, bloque.idBloque);
                            });
                        } else {
                            // No disponible
                            td.addClass("bloque-nodisponible");
                        }

                        tr.append(td);
                    }

                    tbody.append(tr);
                });

                // Inicializar tooltips
                $('[data-bs-toggle="tooltip"]').tooltip();
            },
            error: function() {
                alert("Error al cargar los bloques de horario");
            }
        });
    }


};

// Inicializar la aplicación cuando el DOM esté listo
$(document).ready(function() {
    HorarioApp.init();
});