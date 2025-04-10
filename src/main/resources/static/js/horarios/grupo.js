// Módulo para gestión de grupos
HorarioApp.Grupo = {
    init: function() {
        // Inicializar eventos
        $("#btnGuardarGrupo").click(function() {
            HorarioApp.Grupo.guardarGrupo();
        });

        // Eventos de selección encadenados
        $("#grupoModalidad").change(function() {
            const modalidadId = $(this).val();
            if (!modalidadId) {
                $("#grupoCarrera").empty().append('<option value="">Seleccione modalidad primero...</option>').prop("disabled", true);
                return;
            }

            // Cargar carreras por modalidad
            $.ajax({
                url: `/cursos/carreras/modalidad/${modalidadId}`,
                method: "GET",
                success: function(carreras) {
                    const select = $("#grupoCarrera");
                    select.empty().append('<option value="">Seleccione...</option>');

                    carreras.forEach(carrera => {
                        select.append(new Option(carrera.nombre, carrera.idCarrera));
                    });

                    select.prop("disabled", false);
                }
            });
        });

        $("#grupoCarrera").change(function() {
            const carreraId = $(this).val();
            if (!carreraId) {
                $("#grupoCiclo").empty().append('<option value="">Seleccione carrera primero...</option>').prop("disabled", true);
                return;
            }

            // Cargar ciclos por carrera
            $.ajax({
                url: `/cursos/ciclos/carrera/${carreraId}`,
                method: "GET",
                success: function(ciclos) {
                    const select = $("#grupoCiclo");
                    select.empty().append('<option value="">Seleccione...</option>');

                    ciclos.forEach(ciclo => {
                        select.append(new Option(`Ciclo ${ciclo.numero}`, ciclo.idCiclo));
                    });

                    select.prop("disabled", false);
                }
            });
        });

        // Cargar datos al abrir el modal
        $("#modalGrupo").on('shown.bs.modal', function() {
            // Cargar modalidades para el select
            $.ajax({
                url: "/cursos/modalidades",
                method: "GET",
                success: function(modalidades) {
                    const select = $("#grupoModalidad");
                    select.empty();
                    select.append('<option value="">Seleccione...</option>');

                    modalidades.forEach(modalidad => {
                        select.append(new Option(modalidad.nombre, modalidad.idModalidad));
                    });
                }
            });

            // Cargar tabla de grupos
            HorarioApp.Grupo.cargarGrupos();
        });
    },

    cargarGrupos: function() {
        $.ajax({
            url: "/cursos/grupos",
            method: "GET",
            success: function(grupos) {
                const tbody = $("#tablaGrupos tbody");
                tbody.empty();

                if (grupos.length === 0) {
                    tbody.append(`<tr><td colspan="3" class="text-center">No hay grupos registrados</td></tr>`);
                    return;
                }

                grupos.forEach(grupo => {
                    const tr = $("<tr>");
                    tr.append(`<td>${grupo.nombre}</td>`);
                    tr.append(`<td>${grupo.cicloNombre}</td>`);
                    tr.append(`
                        <td>
                            <button class="btn btn-sm btn-danger btn-eliminar-grupo" data-id="${grupo.idGrupo}">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    `);
                    tbody.append(tr);
                });

                // Evento para eliminar grupo
                $(".btn-eliminar-grupo").click(function() {
                    const grupoId = $(this).data("id");
                    if (confirm("¿Está seguro de eliminar este grupo?")) {
                        HorarioApp.Grupo.eliminarGrupo(grupoId);
                    }
                });
            },
            error: function() {
                alert("Error al cargar los grupos");
            }
        });
    },

    guardarGrupo: function() {
        const cicloId = $("#grupoCiclo").val();
        const nombre = $("#grupoNombre").val();

        if (!cicloId || !nombre) {
            alert("Todos los campos son obligatorios");
            return;
        }

        $.ajax({
            url: "/cursos/grupos",
            method: "POST",
            data: {
                nombre: nombre,
                cicloId: cicloId
            },
            success: function(grupo) {
                // Limpiar el formulario
                $("#formGrupo")[0].reset();
                $("#grupoCarrera, #grupoCiclo").prop("disabled", true);

                // Actualizar la tabla de grupos
                HorarioApp.Grupo.cargarGrupos();

                alert("Grupo guardado con éxito");
            },
            error: function(xhr) {
                if (xhr.status === 400) {
                    alert("Error: " + (xhr.responseJSON ? xhr.responseJSON.message : "No se pudo guardar el grupo"));
                } else {
                    alert("Error al guardar el grupo");
                }
            }
        });
    },

    eliminarGrupo: function(id) {
        $.ajax({
            url: `/cursos/grupos/${id}`,
            method: "DELETE",
            success: function() {
                HorarioApp.Grupo.cargarGrupos();
                alert("Grupo eliminado con éxito");
            },
            error: function(xhr) {
                if (xhr.status === 400) {
                    alert("No se puede eliminar el grupo porque tiene asignaciones asociadas");
                } else {
                    alert("Error al eliminar el grupo");
                }
            }
        });
    }
};