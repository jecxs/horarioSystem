// Módulo para gestión de disponibilidad
HorarioApp.Disponibilidad = {
    init: function() {
        // Inicializar eventos
        $("#btnAgregarDisponibilidad").click(function() {
            HorarioApp.Disponibilidad.agregarDisponibilidad();
        });
    },

    cargarDisponibilidadDocente: function() {
        const tbody = $("#tablaDisponibilidad tbody");
        tbody.empty();

        if (!HorarioApp.docenteSeleccionado.disponibilidades || HorarioApp.docenteSeleccionado.disponibilidades.length === 0) {
            tbody.append(`<tr><td colspan="4" class="text-center">No hay disponibilidades registradas</td></tr>`);
            return;
        }

        HorarioApp.docenteSeleccionado.disponibilidades.forEach(disp => {
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

        // Evento para eliminar disponibilidad
        $(".btn-eliminar-disponibilidad").click(function() {
            const dispId = $(this).data("id");
            if (confirm("¿Está seguro de eliminar esta disponibilidad?")) {
                HorarioApp.Disponibilidad.eliminarDisponibilidad(dispId);
            }
        });
    },

    agregarDisponibilidad: function() {
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

        $.ajax({
            url: `/docentes/${HorarioApp.docenteSeleccionado.idDocente}/disponibilidad`,
            method: "POST",
            data: {
                diaSemana: dia,
                horaInicio: horaInicio,
                horaFin: horaFin
            },
            success: function(disponibilidad) {
                // Actualizar datos del docente
                if (!HorarioApp.docenteSeleccionado.disponibilidades) {
                    HorarioApp.docenteSeleccionado.disponibilidades = [];
                }
                HorarioApp.docenteSeleccionado.disponibilidades.push(disponibilidad);

                // Recargar tabla y horario
                HorarioApp.Disponibilidad.cargarDisponibilidadDocente();
                HorarioApp.cargarHorarioDocente(HorarioApp.docenteSeleccionado.idDocente);

                // Limpiar formulario
                $("#diaSemana").val("");
                $("#horaInicio").val("");
                $("#horaFin").val("");
            },
            error: function() {
                alert("Error al agregar disponibilidad");
            }
        });
    },

    eliminarDisponibilidad: function(id) {
        $.ajax({
            url: `/docentes/disponibilidad/${id}`,
            method: "DELETE",
            success: function() {
                // Actualizar datos del docente
                HorarioApp.docenteSeleccionado.disponibilidades = HorarioApp.docenteSeleccionado.disponibilidades.filter(
                    d => d.id !== id
                );

                // Recargar tabla y horario
                HorarioApp.Disponibilidad.cargarDisponibilidadDocente();
                HorarioApp.cargarHorarioDocente(HorarioApp.docenteSeleccionado.idDocente);
            },
            error: function() {
                alert("Error al eliminar la disponibilidad");
            }
        });
    }
};