// Módulo para gestión de asignaciones
HorarioApp.Asignacion = {
    init: function() {
        // Inicializar eventos
        $("#btnGuardarAsignacion").click(function() {
            HorarioApp.Asignacion.guardarAsignacion();
        });

        // Inicializar eventos de selección encadenados
        $("#asignacionModalidad").change(function() {
            HorarioApp.Asignacion.cargarCarrerasPorModalidad($(this).val());
        });

        $("#asignacionCarrera").change(function() {
            HorarioApp.Asignacion.cargarCiclosPorCarrera($(this).val());
        });

        $("#asignacionCiclo").change(function() {
            const cicloId = $(this).val();
            HorarioApp.Asignacion.cargarCursosPorCiclo(cicloId);
            HorarioApp.Asignacion.cargarGruposPorCiclo(cicloId);
        });

        $("#asignacionTipoSesion").change(function() {
            HorarioApp.Asignacion.cargarAmbientesPorTipoSesion($(this).val());
        });
    },

    iniciarNuevaAsignacion: function(dia, bloqueId) {
        // Preparar el modal de asignación
        $("#modalAsignacion").modal("show");
        $("#asignacionDia").val(dia);

        // Cargar bloques disponibles para ese día
        HorarioApp.Asignacion.cargarBloquesDisponibles(dia, bloqueId);

        // Resetear formulario
        $("#asignacionModalidad").val("");
        $("#asignacionCarrera").val("").prop("disabled", true);
        $("#asignacionCiclo").val("").prop("disabled", true);
        $("#asignacionCurso").val("").prop("disabled", true);
        $("#asignacionGrupo").val("").prop("disabled", true);
        $("#asignacionTipoSesion").val("");
        $("#asignacionAmbiente").val("");
    },

    cargarBloquesDisponibles: function(dia, bloqueIdSeleccionado) {
        const selectBloques = $("#asignacionBloques");
        selectBloques.empty();

        // Filtrar bloques disponibles para el día seleccionado
        const bloquesDisponibles = HorarioApp.horarioData.disponibilidadPorDia[dia].filter(b => b.disponible);

        bloquesDisponibles.forEach(bloque => {
            const option = $("<option>")
                .val(bloque.bloqueId)
                .text(`${bloque.horaInicio.substring(0, 5)} - ${bloque.horaFin.substring(0, 5)}`)
                .prop("selected", bloque.bloqueId === bloqueIdSeleccionado);

            selectBloques.append(option);
        });
    },

    cargarCarrerasPorModalidad: function(modalidadId) {
        if (!modalidadId) {
            $("#asignacionCarrera").empty().append('<option value="">Seleccione modalidad primero...</option>').prop("disabled", true);
            return;
        }

        // Cargar carreras por modalidad
        $.ajax({
            url: `/cursos/carreras/modalidad/${modalidadId}`,
            method: "GET",
            success: function(carreras) {
                const select = $("#asignacionCarrera");
                select.empty().append('<option value="">Seleccione...</option>');

                carreras.forEach(carrera => {
                    select.append(new Option(carrera.nombre, carrera.idCarrera));
                });

                select.prop("disabled", false);
            }
        });
    },

    cargarCiclosPorCarrera: function(carreraId) {
        if (!carreraId) {
            $("#asignacionCiclo").empty().append('<option value="">Seleccione carrera primero...</option>').prop("disabled", true);
            return;
        }

        // Cargar ciclos por carrera
        $.ajax({
            url: `/cursos/ciclos/carrera/${carreraId}`,
            method: "GET",
            success: function(ciclos) {
                const select = $("#asignacionCiclo");
                select.empty().append('<option value="">Seleccione...</option>');

                ciclos.forEach(ciclo => {
                    select.append(new Option(`Ciclo ${ciclo.numero}`, ciclo.idCiclo));
                });

                select.prop("disabled", false);
            }
        });
    },

    cargarCursosPorCiclo: function(cicloId) {
        if (!cicloId) {
            $("#asignacionCurso").empty()
                .append('<option value="">Seleccione ciclo primero...</option>')
                .prop("disabled", true);
            return;
        }

        // Cargar cursos por ciclo
        $.ajax({
            url: `/horarios/cursos/filtrar`,
            method: "GET",
            data: { cicloId: cicloId },
            success: function(cursos) {
                const select = $("#asignacionCurso");
                select.empty().append('<option value="">Seleccione...</option>');

                cursos.forEach(curso => {
                    select.append(new Option(curso.nombre, curso.idCurso));
                });

                select.prop("disabled", false);
            }
        });
    },

    cargarGruposPorCiclo: function(cicloId) {
        if (!cicloId) {
            $("#asignacionGrupo").empty()
                .append('<option value="">Seleccione ciclo primero...</option>')
                .prop("disabled", true);
            return;
        }

        // Cargar grupos por ciclo
        $.ajax({
            url: `/horarios/grupos/ciclo/${cicloId}`,
            method: "GET",
            success: function(grupos) {
                const select = $("#asignacionGrupo");
                select.empty().append('<option value="">Seleccione...</option>');

                grupos.forEach(grupo => {
                    select.append(new Option(grupo.nombre, grupo.idGrupo));
                });

                select.prop("disabled", false);
            }
        });
    },

    cargarAmbientesPorTipoSesion: function(tipoSesion) {
        if (!tipoSesion) {
            $("#asignacionAmbiente").empty().append('<option value="">Seleccione tipo de sesión primero...</option>');
            return;
        }

        const tipoAmbiente = tipoSesion === "Teorica" ? "Teorico" : "Practico";

        // Cargar ambientes por tipo
        $.ajax({
            url: `/horarios/ambientes`,
            method: "GET",
            data: { tipo: tipoAmbiente },
            success: function(ambientes) {
                const select = $("#asignacionAmbiente");
                select.empty().append('<option value="">Seleccione...</option>');

                ambientes.forEach(ambiente => {
                    select.append(new Option(`${ambiente.nombre} (Cap: ${ambiente.capacidad})`, ambiente.idAmbiente));
                });
            }
        });
    },

    guardarAsignacion: function() {
        const docenteId = $("#asignacionDocenteId").val();
        const dia = $("#asignacionDia").val();
        const bloqueIds = Array.from($("#asignacionBloques").val()).map(Number);
        const cursoId = $("#asignacionCurso").val();
        const grupoId = $("#asignacionGrupo").val();
        const tipoSesion = $("#asignacionTipoSesion").val();
        const ambienteId = $("#asignacionAmbiente").val();

        if (!docenteId || !dia || bloqueIds.length === 0 || !cursoId || !grupoId || !tipoSesion || !ambienteId) {
            alert("Todos los campos son obligatorios");
            return;
        }

        const asignacionDTO = {
            docenteId: Number(docenteId),
            diaSemana: dia,
            bloqueIds: bloqueIds,
            cursoId: Number(cursoId),
            grupoId: Number(grupoId),
            tipoSesion: tipoSesion,
            ambienteId: Number(ambienteId)
        };

        // Verificar disponibilidad antes de crear
        $.ajax({
            url: "/horarios/verificar-disponibilidad",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(asignacionDTO),
            success: function(disponible) {
                if (disponible) {
                    HorarioApp.Asignacion.crearAsignacion(asignacionDTO);
                } else {
                    alert("No es posible crear la asignación debido a conflictos de horario");
                }
            },
            error: function() {
                alert("Error al verificar disponibilidad");
            }
        });
    },

    crearAsignacion: function(asignacionDTO) {
        $.ajax({
            url: "/horarios/asignacion",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(asignacionDTO),
            success: function() {
                $("#modalAsignacion").modal("hide");
                // Recargar horario
                HorarioApp.cargarHorarioDocente(HorarioApp.docenteSeleccionado.idDocente);
            },
            error: function() {
                alert("Error al crear la asignación");
            }
        });
    },

    eliminarAsignacion: function(id) {
        $.ajax({
            url: `/horarios/asignacion/${id}`,
            method: "DELETE",
            success: function() {
                // Recargar horario
                HorarioApp.cargarHorarioDocente(HorarioApp.docenteSeleccionado.idDocente);
            },
            error: function() {
                alert("Error al eliminar la asignación");
            }
        });
    }
};