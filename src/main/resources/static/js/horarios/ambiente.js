HorarioApp.Ambiente = {
    init: function() {
        // Inicializar eventos
        $("#btnGuardarAmbiente").click(function() {
            HorarioApp.Ambiente.guardarAmbiente();
        });

        // Inicializar filtros
        $("#filtroAmbienteTipo").change(function() {
            HorarioApp.Ambiente.cargarAmbientes();
        });

        // Inicializar pestañas
        $("#ambienteTab").on("shown.bs.tab", function(e) {
            if (e.target.id === "lista-ambientes-tab") {
                HorarioApp.Ambiente.cargarAmbientes();
            }
        });

        // Cargar ambientes al abrir el modal
        $("#modalAmbiente").on('shown.bs.modal', function() {
            HorarioApp.Ambiente.cargarAmbientes();
            // Limpiar formulario
            $("#ambienteId").val("");
            $("#formAmbiente")[0].reset();
        });
    },

    cargarAmbientes: function() {
        const tipoAmbiente = $("#filtroAmbienteTipo").val();
        const url = tipoAmbiente ?
            `/ambientes?tipo=${tipoAmbiente}` :
            "/ambientes";

        $.ajax({
            url: url,
            method: "GET",
            success: function(ambientes) {
                const tbody = $("#tablaAmbientes tbody");
                tbody.empty();

                if (ambientes.length === 0) {
                    tbody.append(`<tr><td colspan="4" class="text-center">No hay ambientes registrados</td></tr>`);
                    return;
                }

                ambientes.forEach(ambiente => {
                    const tr = $("<tr>");
                    tr.append(`<td>${ambiente.nombre}</td>`);
                    tr.append(`<td>${ambiente.tipo}</td>`);
                    tr.append(`<td>${ambiente.capacidad}</td>`);
                    tr.append(`
                        <td>
                            <button class="btn btn-sm btn-primary btn-editar-ambiente me-1" data-id="${ambiente.idAmbiente}">
                                <i class="bi bi-pencil"></i>
                            </button>
                            <button class="btn btn-sm btn-danger btn-eliminar-ambiente" data-id="${ambiente.idAmbiente}">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    `);
                    tbody.append(tr);
                });

                // Evento para editar ambiente
                $(".btn-editar-ambiente").click(function() {
                    const ambienteId = $(this).data("id");
                    HorarioApp.Ambiente.cargarAmbientePorId(ambienteId);
                });

                // Evento para eliminar ambiente
                $(".btn-eliminar-ambiente").click(function() {
                    const ambienteId = $(this).data("id");
                    if (confirm("¿Está seguro de eliminar este ambiente?")) {
                        HorarioApp.Ambiente.eliminarAmbiente(ambienteId);
                    }
                });
            },
            error: function() {
                alert("Error al cargar los ambientes");
            }
        });
    },

    cargarAmbientePorId: function(id) {
        $.ajax({
            url: `/ambientes/${id}`,
            method: "GET",
            success: function(ambiente) {
                // Cambiar a la pestaña de edición
                $("#nuevo-ambiente-tab").tab("show");

                // Rellenar el formulario
                $("#ambienteId").val(ambiente.idAmbiente);
                $("#ambienteNombre").val(ambiente.nombre);
                $("#ambienteTipo").val(ambiente.tipo);
                $("#ambienteCapacidad").val(ambiente.capacidad);
            },
            error: function() {
                alert("Error al cargar el ambiente");
            }
        });
    },

    guardarAmbiente: function() {
        const id = $("#ambienteId").val();
        const nombre = $("#ambienteNombre").val();
        const tipo = $("#ambienteTipo").val();
        const capacidad = $("#ambienteCapacidad").val();

        if (!nombre || !tipo || !capacidad) {
            alert("Todos los campos son obligatorios");
            return;
        }

        const ambienteDTO = {
            idAmbiente: id || null,
            nombre: nombre,
            tipo: tipo,
            capacidad: parseInt(capacidad)
        };

        const url = id ? `/ambientes/${id}` : "/ambientes";
        const method = id ? "PUT" : "POST";

        $.ajax({
            url: url,
            method: method,
            contentType: "application/json",
            data: JSON.stringify(ambienteDTO),
            success: function() {
                // Limpiar el formulario
                $("#ambienteId").val("");
                $("#formAmbiente")[0].reset();

                // Cambiar a la pestaña de lista
                $("#lista-ambientes-tab").tab("show");

                // Actualizar la lista de ambientes
                HorarioApp.Ambiente.cargarAmbientes();

                alert("Ambiente guardado con éxito");
            },
            error: function(xhr) {
                if (xhr.status === 400) {
                    alert("Error: " + (xhr.responseJSON ? xhr.responseJSON.message : "No se pudo guardar el ambiente"));
                } else {
                    alert("Error al guardar el ambiente");
                }
            }
        });
    },

    eliminarAmbiente: function(id) {
        $.ajax({
            url: `/ambientes/${id}`,
            method: "DELETE",
            success: function() {
                HorarioApp.Ambiente.cargarAmbientes();
                alert("Ambiente eliminado con éxito");
            },
            error: function(xhr) {
                if (xhr.status === 400) {
                    alert("No se puede eliminar el ambiente porque tiene asignaciones asociadas");
                } else {
                    alert("Error al eliminar el ambiente");
                }
            }
        });
    }
};