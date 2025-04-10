// Módulo para gestión de configuración
HorarioApp.Configuracion = {
    init: function() {
        // Inicialización general
        this.Modalidad.init();
        this.Carrera.init();
        this.Ciclo.init();
        this.Curso.init();
        // Más inicializaciones si son necesarias
    },

    // Sub-módulo para modalidades
    Modalidad: {
        init: function() {
            $("#btnGuardarModalidad").click(function() {
                HorarioApp.Configuracion.Modalidad.guardarModalidad();
            });

            // Cargar modalidades al abrir el modal
            $("#modalModalidad").on('shown.bs.modal', function() {
                HorarioApp.Configuracion.Modalidad.cargarModalidades();
            });
        },

        cargarModalidades: function() {
            $.ajax({
                url: "/cursos/modalidades",
                method: "GET",
                success: function(modalidades) {
                    const tbody = $("#tablaModalidades tbody");
                    tbody.empty();

                    if (modalidades.length === 0) {
                        tbody.append(`<tr><td colspan="3" class="text-center">No hay modalidades registradas</td></tr>`);
                        return;
                    }

                    modalidades.forEach(modalidad => {
                        const tr = $("<tr>");
                        tr.append(`<td>${modalidad.nombre}</td>`);
                        tr.append(`<td>${modalidad.duracionAnios} año(s)</td>`);
                        tr.append(`
                            <td>
                                <button class="btn btn-sm btn-danger btn-eliminar-modalidad" data-id="${modalidad.idModalidad}">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        `);
                        tbody.append(tr);
                    });

                    // Evento para eliminar modalidad
                    $(".btn-eliminar-modalidad").click(function() {
                        const modalidadId = $(this).data("id");
                        if (confirm("¿Está seguro de eliminar esta modalidad educativa?")) {
                            HorarioApp.Configuracion.Modalidad.eliminarModalidad(modalidadId);
                        }
                    });
                },
                error: function() {
                    alert("Error al cargar las modalidades educativas");
                }
            });
        },

        guardarModalidad: function() {
            const nombre = $("#modalidadNombre").val();
            const duracion = $("#modalidadDuracion").val();

            if (!nombre || !duracion) {
                alert("Todos los campos son obligatorios");
                return;
            }

            $.ajax({
                url: "/cursos/modalidades",
                method: "POST",
                data: {
                    nombre: nombre,
                    duracionAnios: duracion
                },
                success: function(modalidad) {
                    // Limpiar el formulario
                    $("#formModalidad")[0].reset();

                    // Actualizar la tabla de modalidades
                    HorarioApp.Configuracion.Modalidad.cargarModalidades();

                    // Actualizar selectores
                    HorarioApp.Configuracion.actualizarSelectoresModalidad(modalidad);

                    alert("Modalidad educativa guardada con éxito");
                },
                error: function(xhr) {
                    if (xhr.status === 400) {
                        alert("Error: " + (xhr.responseJSON ? xhr.responseJSON.message : "No se pudo guardar la modalidad"));
                    } else {
                        alert("Error al guardar la modalidad educativa");
                    }
                }
            });
        },

        eliminarModalidad: function(id) {
            $.ajax({
                url: `/cursos/modalidades/${id}`,
                method: "DELETE",
                success: function() {
                    HorarioApp.Configuracion.Modalidad.cargarModalidades();

                    // Actualizar selectores eliminando la opción
                    $(`#asignacionModalidad option[value="${id}"]`).remove();
                    $(`#cursoModalidad option[value="${id}"]`).remove();
                    $(`#filtroCursoModalidad option[value="${id}"]`).remove();
                    $(`#carreraModalidad option[value="${id}"]`).remove();
                    $(`#cicloModalidad option[value="${id}"]`).remove();

                    alert("Modalidad eliminada con éxito");
                },
                error: function(xhr) {
                    if (xhr.status === 400) {
                        alert("No se puede eliminar la modalidad porque tiene carreras asociadas");
                    } else {
                        alert("Error al eliminar la modalidad");
                    }
                }
            });
        }
    },

    // Sub-módulo para carreras
    Carrera: {
        init: function() {
            $("#btnGuardarCarrera").click(function() {
                HorarioApp.Configuracion.Carrera.guardarCarrera();
            });

            // Cargar modalidades y carreras al abrir el modal
            $("#modalCarrera").on('shown.bs.modal', function() {
                // Cargar modalidades para el select
                $.ajax({
                    url: "/cursos/modalidades",
                    method: "GET",
                    success: function(modalidades) {
                        const select = $("#carreraModalidad");
                        select.empty();
                        select.append('<option value="">Seleccione...</option>');

                        modalidades.forEach(modalidad => {
                            select.append(new Option(modalidad.nombre, modalidad.idModalidad));
                        });
                    }
                });

                // Cargar tabla de carreras
                HorarioApp.Configuracion.Carrera.cargarCarreras();
            });
        },

        cargarCarreras: function() {
            $.ajax({
                url: "/cursos/carreras",
                method: "GET",
                success: function(carreras) {
                    const tbody = $("#tablaCarreras tbody");
                    tbody.empty();

                    if (carreras.length === 0) {
                        tbody.append(`<tr><td colspan="3" class="text-center">No hay carreras registradas</td></tr>`);
                        return;
                    }

                    carreras.forEach(carrera => {
                        const tr = $("<tr>");
                        tr.append(`<td>${carrera.nombre}</td>`);
                        tr.append(`<td>${carrera.modalidad.nombre}</td>`);
                        tr.append(`
                            <td>
                                <button class="btn btn-sm btn-danger btn-eliminar-carrera" data-id="${carrera.idCarrera}">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        `);
                        tbody.append(tr);
                    });

                    // Evento para eliminar carrera
                    $(".btn-eliminar-carrera").click(function() {
                        const carreraId = $(this).data("id");
                        if (confirm("¿Está seguro de eliminar esta carrera?")) {
                            HorarioApp.Configuracion.Carrera.eliminarCarrera(carreraId);
                        }
                    });
                },
                error: function() {
                    alert("Error al cargar las carreras");
                }
            });
        },

        guardarCarrera: function() {
            const modalidadId = $("#carreraModalidad").val();
            const nombre = $("#carreraNombre").val();

            if (!modalidadId || !nombre) {
                alert("Todos los campos son obligatorios");
                return;
            }

            $.ajax({
                url: "/cursos/carreras",
                method: "POST",
                data: {
                    nombre: nombre,
                    modalidadId: modalidadId
                },
                success: function(carrera) {
                    // Limpiar el formulario
                    $("#formCarrera")[0].reset();

                    // Actualizar la tabla de carreras
                    HorarioApp.Configuracion.Carrera.cargarCarreras();

                    alert("Carrera guardada con éxito");
                },
                error: function(xhr) {
                    if (xhr.status === 400) {
                        alert("Error: " + (xhr.responseJSON ? xhr.responseJSON.message : "No se pudo guardar la carrera"));
                    } else {
                        alert("Error al guardar la carrera");
                    }
                }
            });
        },

        eliminarCarrera: function(id) {
            $.ajax({
                url: `/cursos/carreras/${id}`,
                method: "DELETE",
                success: function() {
                    HorarioApp.Configuracion.Carrera.cargarCarreras();
                    alert("Carrera eliminada con éxito");
                },
                error: function(xhr) {
                    if (xhr.status === 400) {
                        alert("No se puede eliminar la carrera porque tiene ciclos asociados");
                    } else {
                        alert("Error al eliminar la carrera");
                    }
                }
            });
        }
    },

    // Sub-módulo para ciclos
    Ciclo: {
        init: function() {
            $("#btnGuardarCiclo").click(function() {
                HorarioApp.Configuracion.Ciclo.guardarCiclo();
            });

            // Eventos de selección encadenados
            $("#cicloModalidad").change(function() {
                const modalidadId = $(this).val();
                if (!modalidadId) {
                    $("#cicloCarrera").empty().append('<option value="">Seleccione modalidad primero...</option>').prop("disabled", true);
                    return;
                }

                // Cargar carreras por modalidad
                $.ajax({
                    url: `/cursos/carreras/modalidad/${modalidadId}`,
                    method: "GET",
                    success: function(carreras) {
                        const select = $("#cicloCarrera");
                        select.empty().append('<option value="">Seleccione...</option>');

                        carreras.forEach(carrera => {
                            select.append(new Option(carrera.nombre, carrera.idCarrera));
                        });

                        select.prop("disabled", false);
                    }
                });
            });

            // Cargar datos al abrir el modal
            $("#modalCiclo").on('shown.bs.modal', function() {
                // Cargar modalidades para el select
                $.ajax({
                    url: "/cursos/modalidades",
                    method: "GET",
                    success: function(modalidades) {
                        const select = $("#cicloModalidad");
                        select.empty();
                        select.append('<option value="">Seleccione...</option>');

                        modalidades.forEach(modalidad => {
                            select.append(new Option(modalidad.nombre, modalidad.idModalidad));
                        });
                    }
                });

                // Cargar tabla de ciclos
                HorarioApp.Configuracion.Ciclo.cargarCiclos();
            });
        },

        cargarCiclos: function() {
            $.ajax({
                url: "/cursos/ciclos",
                method: "GET",
                success: function(ciclos) {
                    const tbody = $("#tablaCiclos tbody");
                    tbody.empty();

                    if (ciclos.length === 0) {
                        tbody.append(`<tr><td colspan="4" class="text-center">No hay ciclos registrados</td></tr>`);
                        return;
                    }

                    ciclos.forEach(ciclo => {
                        const tr = $("<tr>");
                        tr.append(`<td>Ciclo ${ciclo.numero}</td>`);
                        tr.append(`<td>${ciclo.carrera.nombre}</td>`);
                        tr.append(`<td>${ciclo.carrera.modalidad.nombre}</td>`);
                        tr.append(`
                            <td>
                                <button class="btn btn-sm btn-danger btn-eliminar-ciclo" data-id="${ciclo.idCiclo}">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        `);
                        tbody.append(tr);
                    });

                    // Evento para eliminar ciclo
                    $(".btn-eliminar-ciclo").click(function() {
                        const cicloId = $(this).data("id");
                        if (confirm("¿Está seguro de eliminar este ciclo?")) {
                            HorarioApp.Configuracion.Ciclo.eliminarCiclo(cicloId);
                        }
                    });
                },
                error: function() {
                    alert("Error al cargar los ciclos");
                }
            });
        },

        guardarCiclo: function() {
            const carreraId = $("#cicloCarrera").val();
            const numero = $("#cicloNumero").val();

            if (!carreraId || !numero) {
                alert("Todos los campos son obligatorios");
                return;
            }

            $.ajax({
                url: "/cursos/ciclos",
                method: "POST",
                data: {
                    numero: numero,
                    carreraId: carreraId
                },
                success: function(ciclo) {
                    // Limpiar el formulario
                    $("#formCiclo")[0].reset();
                    $("#cicloCarrera").prop("disabled", true);

                    // Actualizar la tabla de ciclos
                    HorarioApp.Configuracion.Ciclo.cargarCiclos();

                    alert("Ciclo guardado con éxito");
                },
                error: function(xhr) {
                    if (xhr.status === 400) {
                        alert("Error: " + (xhr.responseJSON ? xhr.responseJSON.message : "No se pudo guardar el ciclo"));
                    } else {
                        alert("Error al guardar el ciclo");
                    }
                }
            });
        },

        eliminarCiclo: function(id) {
            $.ajax({
                url: `/cursos/ciclos/${id}`,
                method: "DELETE",
                success: function() {
                    HorarioApp.Configuracion.Ciclo.cargarCiclos();
                    alert("Ciclo eliminado con éxito");
                },
                error: function(xhr) {
                    if (xhr.status === 400) {
                        alert("No se puede eliminar el ciclo porque tiene cursos o grupos asociados");
                    } else {
                        alert("Error al eliminar el ciclo");
                    }
                }
            });
        }
    },

    // Sub-módulo para cursos
    Curso: {
        init: function() {
            // Inicializar pestañas
            $("#cursoTab").on("shown.bs.tab", function(e) {
                if (e.target.id === "lista-cursos-tab") {
                    HorarioApp.Configuracion.Curso.cargarCursos();
                }
            });

            // Inicializar filtros
            $("#filtroCursoModalidad").change(function() {
                HorarioApp.Configuracion.Curso.cargarCursos();
            });

            // Eventos de selección encadenados
            $("#cursoModalidad").change(function() {
                const modalidadId = $(this).val();
                if (!modalidadId) {
                    $("#cursoCarrera").empty().append('<option value="">Seleccione modalidad primero...</option>').prop("disabled", true);
                    return;
                }

                // Cargar carreras por modalidad
                $.ajax({
                    url: `/cursos/carreras/modalidad/${modalidadId}`,
                    method: "GET",
                    success: function(carreras) {
                        const select = $("#cursoCarrera");
                        select.empty().append('<option value="">Seleccione...</option>');

                        carreras.forEach(carrera => {
                            select.append(new Option(carrera.nombre, carrera.idCarrera));
                        });

                        select.prop("disabled", false);
                    }
                });
            });

            $("#cursoCarrera").change(function() {
                const carreraId = $(this).val();
                if (!carreraId) {
                    $("#cursoCiclo").empty().append('<option value="">Seleccione carrera primero...</option>').prop("disabled", true);
                    return;
                }

                // Cargar ciclos por carrera
                $.ajax({
                    url: `/cursos/ciclos/carrera/${carreraId}`,
                    method: "GET",
                    success: function(ciclos) {
                        const select = $("#cursoCiclo");
                        select.empty().append('<option value="">Seleccione...</option>');

                        ciclos.forEach(ciclo => {
                            select.append(new Option(`Ciclo ${ciclo.numero}`, ciclo.idCiclo));
                        });

                        select.prop("disabled", false);
                    }
                });
            });

            // Guardar curso
            $("#btnGuardarCurso").click(function() {
                HorarioApp.Configuracion.Curso.guardarCurso();
            });

            // Cargar cursos al abrir el modal
            $("#modalCurso").on('shown.bs.modal', function() {
                HorarioApp.Configuracion.Curso.cargarCursos();

                // Cargar modalidades para el filtro y el formulario
                $.ajax({
                    url: "/cursos/modalidades",
                    method: "GET",
                    success: function(modalidades) {
                        $("#cursoModalidad, #filtroCursoModalidad").each(function() {
                            const select = $(this);
                            const isFilter = select.attr('id') === 'filtroCursoModalidad';

                            select.empty();
                            select.append(`<option value="">${isFilter ? 'Todas las modalidades' : 'Seleccione...'}</option>`);

                            modalidades.forEach(modalidad => {
                                select.append(new Option(modalidad.nombre, modalidad.idModalidad));
                            });
                        });
                    }
                });
            });
        },

        cargarCursos: function() {
            const modalidadId = $("#filtroCursoModalidad").val();
            const filtros = { modalidadId: modalidadId || null };

            $.ajax({
                url: `/horarios/cursos/filtrar`,
                method: "GET",
                data: filtros,
                success: function(cursos) {
                    const tbody = $("#tablaCursos tbody");
                    tbody.empty();

                    if (cursos.length === 0) {
                        tbody.append(`<tr><td colspan="5" class="text-center">No hay cursos registrados</td></tr>`);
                        return;
                    }

                    cursos.forEach(curso => {
                        const tr = $("<tr>");
                        tr.append(`<td>${curso.nombre}</td>`);
                        tr.append(`<td>${curso.cicloNombre}</td>`);
                        tr.append(`<td>${curso.carreraNombre}</td>`);
                        tr.append(`<td>${curso.tipo}</td>`);
                        tr.append(`
                            <td>
                                <button class="btn btn-sm btn-danger btn-eliminar-curso" data-id="${curso.idCurso}">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        `);
                        tbody.append(tr);
                    });

                    // Evento para eliminar curso
                    $(".btn-eliminar-curso").click(function() {
                        const cursoId = $(this).data("id");
                        if (confirm("¿Está seguro de eliminar este curso?")) {
                            HorarioApp.Configuracion.Curso.eliminarCurso(cursoId);
                        }
                    });
                }
            });
        },

        guardarCurso: function() {
            const cicloId = $("#cursoCiclo").val();
            const nombre = $("#cursoNombre").val();
            const tipo = $("#cursoTipo").val();
            const horas = $("#cursoHoras").val();

            if (!cicloId || !nombre || !tipo || !horas) {
                alert("Todos los campos son obligatorios");
                return;
            }

            const cursoDTO = {
                nombre: nombre,
                tipo: tipo,
                horasSemana: parseInt(horas)
            };

            $.ajax({
                url: `/cursos?cicloId=${cicloId}`,
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(cursoDTO),
                success: function(curso) {
                    // Limpiar el formulario
                    $("#formCurso")[0].reset();
                    $("#cursoCarrera, #cursoCiclo").prop("disabled", true);

                    // Cambiar a la pestaña de lista
                    $("#lista-cursos-tab").tab("show");

                    // Actualizar la lista de cursos
                    HorarioApp.Configuracion.Curso.cargarCursos();

                    alert("Curso guardado con éxito");
                },
                error: function(xhr) {
                    if (xhr.status === 400) {
                        alert("Error: " + (xhr.responseJSON ? xhr.responseJSON.message : "No se pudo guardar el curso"));
                    } else {
                        alert("Error al guardar el curso");
                    }
                }
            });
        },

        eliminarCurso: function(id) {
            $.ajax({
                url: `/cursos/${id}`,
                method: "DELETE",
                success: function() {
                    HorarioApp.Configuracion.Curso.cargarCursos();
                    alert("Curso eliminado con éxito");
                },
                error: function(xhr) {
                    if (xhr.status === 400) {
                        alert("No se puede eliminar el curso porque tiene asignaciones asociadas");
                    } else {
                        alert("Error al eliminar el curso");
                    }
                }
            });
        }
    },

    // Función auxiliar para actualizar todos los selectores de modalidad
    actualizarSelectoresModalidad: function(modalidad) {
        // Actualizar todos los selectores que muestran modalidades
        const selectores = ["#carreraModalidad", "#cicloModalidad", "#cursoModalidad", "#asignacionModalidad", "#filtroCursoModalidad"];

        selectores.forEach(selector => {
            const select = $(selector);

            // Si el selector existe en la página actual
            if (select.length > 0) {
                const option = new Option(modalidad.nombre, modalidad.idModalidad);
                select.append(option);
            }
        });
    }
};