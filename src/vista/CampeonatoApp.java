// Archivo: view/CampeonatoApp.java
package vista;

import model.*; // Import all model classes
import util.JsonDataManager; // Import JsonDataManager
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter; // For saving on close
import java.awt.event.WindowEvent; // For saving on close
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CampeonatoApp extends JFrame {
    private Campeonato campeonato;
    private JsonDataManager dataManager; // Instancia del gestor de datos

    // --- Componentes del Panel de Equipos ---
    private DefaultTableModel equipoTableModel;
    private JTable equipoTable;
    private JTextField equipoIdField, equipoNombreField, equipoBarrioField, equipoEntrenadorField;

    // --- Componentes del Panel de Jugadores ---
    private DefaultTableModel jugadorTableModel;
    private JTable jugadorTable;
    private JTextField jugadorIdField, jugadorNombreCompletoField, jugadorPosicionField, jugadorNumeroCamisetaField;
    private JComboBox<Equipo> jugadorEquipoComboBox;

    // --- Componentes del Panel de Partidos ---
    private DefaultTableModel partidoTableModel;
    private JTable partidoTable;
    private JTextField partidoIdField, partidoEstadioField, partidoArbitroField, partidoFechaHoraField;
    private JComboBox<Equipo> partidoEquipoLocalComboBox, partidoEquipoVisitanteComboBox;

    // --- Componentes del Panel de Estadísticas ---
    private DefaultTableModel tablaPosicionesModel;
    private JTable tablaPosicionesTable;
    private JComboBox<Equipo> estadisticasEquipoComboBox;
    private JLabel partidosGanadosLabel, partidosEmpatadosLabel, partidosPerdidosLabel,
            golesAFavorLabel, golesEnContraLabel, puntosLabel, totalTarjetasLabel, totalFaltasLabel;
    private JComboBox<Jugador> estadisticasJugadorComboBox;
    private JLabel golesJugadorLabel, tarjetasJugadorLabel, faltasJugadorLabel, partidosJugadosJugadorLabel;


    public CampeonatoApp() {
        super("Gestión de Campeonato de Fútbol");

        // Inicializar el gestor de datos y cargar el campeonato
        dataManager = new JsonDataManager("campeonato_data.json");
        campeonato = dataManager.load();

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Añadir un WindowListener para guardar los datos al cerrar la aplicación
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dataManager.save(campeonato);
                super.windowClosing(e);
            }
        });

        JTabbedPane tabbedPane = new JTabbedPane();

        // Inicializar y añadir cada panel
        tabbedPane.addTab("Equipos", createEquipoPanel());
        tabbedPane.addTab("Jugadores", createJugadorPanel());
        tabbedPane.addTab("Partidos", createPartidoPanel());
        tabbedPane.addTab("Estadísticas", createEstadisticasPanel());

        add(tabbedPane);

        // Actualizaciones iniciales para todas las tablas/comboboxes
        updateEquipoTable();
        updateJugadorEquipoComboBox(); // Para el Panel de Jugadores
        updateJugadorTable();
        updatePartidoEquipoComboBoxes(); // Para el Panel de Partidos
        updatePartidoTable();
        updateAllStats(); // Para el Panel de Estadísticas

        setVisible(true);
    }

    // --- Creación y Lógica del Panel de Equipos ---
    private JPanel createEquipoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Equipo"));

        equipoIdField = new JTextField(15);
        equipoNombreField = new JTextField(15);
        equipoBarrioField = new JTextField(15);
        equipoEntrenadorField = new JTextField(15);

        formPanel.add(new JLabel("ID:"));
        formPanel.add(equipoIdField);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(equipoNombreField);
        formPanel.add(new JLabel("Barrio:"));
        formPanel.add(equipoBarrioField);
        formPanel.add(new JLabel("Entrenador:"));
        formPanel.add(equipoEntrenadorField);

        JButton registrarButton = new JButton("Registrar Equipo");
        registrarButton.addActionListener(e -> registrarEquipo());
        formPanel.add(registrarButton);
        formPanel.add(new JLabel("")); // Espacio en blanco para la cuadrícula

        panel.add(formPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Nombre", "Barrio", "Entrenador"};
        equipoTableModel = new DefaultTableModel(columnNames, 0);
        equipoTable = new JTable(equipoTableModel);
        equipoTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(equipoTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void registrarEquipo() {
        String id = equipoIdField.getText().trim();
        String nombre = equipoNombreField.getText().trim();
        String barrio = equipoBarrioField.getText().trim();
        String entrenador = equipoEntrenadorField.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || barrio.isEmpty() || entrenador.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Equipo nuevoEquipo = new Equipo(id, nombre, barrio, entrenador);
            campeonato.registrarEquipo(id, nombre, barrio, entrenador);
            JOptionPane.showMessageDialog(this, "Equipo registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            clearEquipoFields();
            updateEquipoTable();
            updateJugadorEquipoComboBox(); // Actualizar el ComboBox de equipos en el panel de jugadores
            updatePartidoEquipoComboBoxes(); // Actualizar los ComboBoxes de equipos en el panel de partidos
            updateAllStats(); // Actualizar todas las estadísticas ya que la lista de equipos ha cambiado
            dataManager.save(campeonato); // Guardar cambios
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearEquipoFields() {
        equipoIdField.setText("");
        equipoNombreField.setText("");
        equipoBarrioField.setText("");
        equipoEntrenadorField.setText("");
    }

    private void updateEquipoTable() {
        equipoTableModel.setRowCount(0); // Limpiar la tabla
        for (Equipo equipo : campeonato.getEquipos()) {
            equipoTableModel.addRow(new Object[]{equipo.getId(), equipo.getNombre(), equipo.getBarrio(), equipo.getNombreEntrenador()});
        }
    }

    // --- Creación y Lógica del Panel de Jugadores ---
    private JPanel createJugadorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Jugador"));

        jugadorIdField = new JTextField(15);
        jugadorNombreCompletoField = new JTextField(15);
        jugadorPosicionField = new JTextField(15);
        jugadorNumeroCamisetaField = new JTextField(15);
        jugadorEquipoComboBox = new JComboBox<>();

        formPanel.add(new JLabel("ID:"));
        formPanel.add(jugadorIdField);
        formPanel.add(new JLabel("Nombre Completo:"));
        formPanel.add(jugadorNombreCompletoField);
        formPanel.add(new JLabel("Posición:"));
        formPanel.add(jugadorPosicionField);
        formPanel.add(new JLabel("Número Camiseta:"));
        formPanel.add(jugadorNumeroCamisetaField);
        formPanel.add(new JLabel("Equipo:"));
        formPanel.add(jugadorEquipoComboBox);

        JButton registrarButton = new JButton("Registrar Jugador");
        registrarButton.addActionListener(e -> registrarJugador());
        formPanel.add(registrarButton);
        formPanel.add(new JLabel("")); // Espacio en blanco para la cuadrícula

        panel.add(formPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Nombre Completo", "Posición", "Número Camiseta", "Equipo"};
        jugadorTableModel = new DefaultTableModel(columnNames, 0);
        jugadorTable = new JTable(jugadorTableModel);
        jugadorTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(jugadorTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void registrarJugador() {
        String id = jugadorIdField.getText().trim();
        String nombreCompleto = jugadorNombreCompletoField.getText().trim();
        String posicion = jugadorPosicionField.getText().trim();
        String numeroCamisetaStr = jugadorNumeroCamisetaField.getText().trim();
        Equipo equipoSeleccionado = (Equipo) jugadorEquipoComboBox.getSelectedItem();

        if (id.isEmpty() || nombreCompleto.isEmpty() || posicion.isEmpty() || numeroCamisetaStr.isEmpty() || equipoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int numeroCamiseta = Integer.parseInt(numeroCamisetaStr);
            Jugador nuevoJugador = new Jugador(id, nombreCompleto, posicion, numeroCamiseta, equipoSeleccionado);

            // Verificar si el jugador ya existe por ID en el equipo seleccionado
            boolean jugadorExiste = equipoSeleccionado.getJugadores().stream().anyMatch(j -> j.getId().equals(id));
            if (jugadorExiste) {
                JOptionPane.showMessageDialog(this, "Ya existe un jugador con el ID " + id + " en este equipo.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            equipoSeleccionado.getJugadores().add(nuevoJugador);
            JOptionPane.showMessageDialog(this, "Jugador registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            clearJugadorFields();
            updateJugadorTable();
            updateAllStats(); // Actualizar todas las estadísticas ya que la lista de jugadores ha cambiado
            dataManager.save(campeonato); // Guardar cambios
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El número de camiseta debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar jugador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearJugadorFields() {
        jugadorIdField.setText("");
        jugadorNombreCompletoField.setText("");
        jugadorPosicionField.setText("");
        jugadorNumeroCamisetaField.setText("");
        jugadorEquipoComboBox.setSelectedIndex(-1);
    }

    private void updateJugadorEquipoComboBox() {
        jugadorEquipoComboBox.removeAllItems();
        for (Equipo equipo : campeonato.getEquipos()) {
            jugadorEquipoComboBox.addItem(equipo);
        }
    }

    private void updateJugadorTable() {
        jugadorTableModel.setRowCount(0); // Limpiar la tabla
        for (Equipo equipo : campeonato.getEquipos()) {
            for (Jugador jugador : equipo.getJugadores()) {
                jugadorTableModel.addRow(new Object[]{jugador.getId(), jugador.getNombreCompleto(), jugador.getPosicion(), jugador.getNumeroCamiseta(), jugador.getEquipo().getNombre()});
            }
        }
    }

    // --- Creación y Lógica del Panel de Partidos ---
    private JPanel createPartidoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Crear Nuevo Partido"));

        partidoIdField = new JTextField(15);
        partidoEstadioField = new JTextField(15);
        partidoArbitroField = new JTextField(15);
        partidoFechaHoraField = new JTextField(15); // Formato:yyyy-MM-dd HH:mm
        partidoEquipoLocalComboBox = new JComboBox<>();
        partidoEquipoVisitanteComboBox = new JComboBox<>();

        formPanel.add(new JLabel("ID Partido:"));
        formPanel.add(partidoIdField);
        formPanel.add(new JLabel("Equipo Local:"));
        formPanel.add(partidoEquipoLocalComboBox);
        formPanel.add(new JLabel("Equipo Visitante:"));
        formPanel.add(partidoEquipoVisitanteComboBox);
        formPanel.add(new JLabel("Estadio:"));
        formPanel.add(partidoEstadioField);
        formPanel.add(new JLabel("Árbitro:"));
        formPanel.add(partidoArbitroField);
        formPanel.add(new JLabel("Fecha y Hora (yyyy-MM-dd HH:mm):"));
        formPanel.add(partidoFechaHoraField);

        JButton crearPartidoButton = new JButton("Crear Partido");
        crearPartidoButton.addActionListener(e -> crearPartido());
        formPanel.add(crearPartidoButton);
        formPanel.add(new JLabel("")); // Espacio en blanco

        panel.add(formPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Local", "Visitante", "Estadio", "Goles Local", "Goles Visitante", "Fecha/Hora"};
        partidoTableModel = new DefaultTableModel(columnNames, 0);
        partidoTable = new JTable(partidoTableModel);
        partidoTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(partidoTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones para el partido seleccionado
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Acciones del Partido Seleccionado"));

        JButton registrarGolButton = new JButton("Registrar Gol");
        JButton registrarTarjetaButton = new JButton("Registrar Tarjeta");
        JButton registrarFaltaButton = new JButton("Registrar Falta");

        registrarGolButton.addActionListener(e -> openRegistrarGolDialog());
        registrarTarjetaButton.addActionListener(e -> openRegistrarTarjetaDialog());
        registrarFaltaButton.addActionListener(e -> openRegistrarFaltaDialog());

        actionsPanel.add(registrarGolButton);
        actionsPanel.add(registrarTarjetaButton);
        actionsPanel.add(registrarFaltaButton);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void crearPartido() {
        String id = partidoIdField.getText().trim();
        Equipo equipoLocal = (Equipo) partidoEquipoLocalComboBox.getSelectedItem();
        Equipo equipoVisitante = (Equipo) partidoEquipoVisitanteComboBox.getSelectedItem();
        String estadio = partidoEstadioField.getText().trim();
        String arbitro = partidoArbitroField.getText().trim();
        String fechaHoraStr = partidoFechaHoraField.getText().trim();

        if (id.isEmpty() || equipoLocal == null || equipoVisitante == null || estadio.isEmpty() || arbitro.isEmpty() || fechaHoraStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (equipoLocal.equals(equipoVisitante)) {
            JOptionPane.showMessageDialog(this, "El equipo local y visitante no pueden ser el mismo.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            campeonato.crearPartido(id, equipoLocal.getId(), equipoVisitante.getId(), estadio, arbitro);
            JOptionPane.showMessageDialog(this, "Partido creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            clearPartidoFields();
            updatePartidoTable();
            updateAllStats(); // Actualizar todas las estadísticas ya que la lista de partidos ha cambiado
            dataManager.save(campeonato); // Guardar cambios
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha y hora inválido. UsebeginPath-MM-dd HH:mm", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Creación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearPartidoFields() {
        partidoIdField.setText("");
        partidoEstadioField.setText("");
        partidoArbitroField.setText("");
        partidoFechaHoraField.setText("");
        partidoEquipoLocalComboBox.setSelectedIndex(-1);
        partidoEquipoVisitanteComboBox.setSelectedIndex(-1);
    }

    private void updatePartidoEquipoComboBoxes() {
        partidoEquipoLocalComboBox.removeAllItems();
        partidoEquipoVisitanteComboBox.removeAllItems();
        for (Equipo equipo : campeonato.getEquipos()) {
            partidoEquipoLocalComboBox.addItem(equipo);
            partidoEquipoVisitanteComboBox.addItem(equipo);
        }
    }

    private void updatePartidoTable() {
        partidoTableModel.setRowCount(0); // Limpiar la tabla
        for (Partido partido : campeonato.getPartidos()) {
            partidoTableModel.addRow(new Object[]{
                    partido.getId(),
                    partido.getEquipoLocal() != null ? partido.getEquipoLocal().getNombre() : "N/A",
                    partido.getEquipoVisitante() != null ? partido.getEquipoVisitante().getNombre() : "N/A",
                    partido.getEstadio(),
                    partido.getGolesLocal(),
                    partido.getGolesVisitante(),
            });
        }
    }

    private Partido getSelectedPartido() {
        int selectedRow = partidoTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un partido de la tabla.", "Ningún Partido Seleccionado", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String partidoId = (String) partidoTableModel.getValueAt(selectedRow, 0);
        return campeonato.getPartidos().stream()
                .filter(p -> p.getId().equals(partidoId))
                .findFirst()
                .orElse(null);
    }

    private void openRegistrarGolDialog() {
        Partido selectedPartido = getSelectedPartido();
        if (selectedPartido == null) return;

        // Obtener todos los jugadores de ambos equipos del partido
        List<Jugador> jugadoresEnPartido = new ArrayList<>();
        if (selectedPartido.getEquipoLocal() != null) {
            jugadoresEnPartido.addAll(selectedPartido.getEquipoLocal().getJugadores());
        }
        if (selectedPartido.getEquipoVisitante() != null) {
            jugadoresEnPartido.addAll(selectedPartido.getEquipoVisitante().getJugadores());
        }


        if (jugadoresEnPartido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay jugadores registrados para este partido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<Jugador> jugadorComboBox = new JComboBox<>(jugadoresEnPartido.toArray(new Jugador[0]));
        JTextField minutoField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Jugador:"));
        panel.add(jugadorComboBox);
        panel.add(new JLabel("Minuto:"));
        panel.add(minutoField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Gol para " + selectedPartido.toString(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Jugador jugador = (Jugador) jugadorComboBox.getSelectedItem();
                int minuto = Integer.parseInt(minutoField.getText().trim());

                if (jugador == null || minuto <= 0) {
                    JOptionPane.showMessageDialog(this, "Datos inválidos para el gol.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                campeonato.registrarGol(selectedPartido.getId(), jugador.getId(), minuto);
                JOptionPane.showMessageDialog(this, "Gol registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                updatePartidoTable();
                updateAllStats(); // Notificar cambio en estadísticas
                dataManager.save(campeonato); // Guardar cambios
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El minuto debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al Registrar Gol", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openRegistrarTarjetaDialog() {
        Partido selectedPartido = getSelectedPartido();
        if (selectedPartido == null) return;

        List<Jugador> jugadoresEnPartido = new ArrayList<>();
        if (selectedPartido.getEquipoLocal() != null) {
            jugadoresEnPartido.addAll(selectedPartido.getEquipoLocal().getJugadores());
        }
        if (selectedPartido.getEquipoVisitante() != null) {
            jugadoresEnPartido.addAll(selectedPartido.getEquipoVisitante().getJugadores());
        }

        if (jugadoresEnPartido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay jugadores registrados para este partido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<Jugador> jugadorComboBox = new JComboBox<>(jugadoresEnPartido.toArray(new Jugador[0]));
        String[] tiposTarjeta = {"Amarilla", "Roja"};
        JComboBox<String> tipoTarjetaComboBox = new JComboBox<>(tiposTarjeta);
        JTextField minutoField = new JTextField(5);
        JTextField motivoField = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Jugador:"));
        panel.add(jugadorComboBox);
        panel.add(new JLabel("Tipo de Tarjeta:"));
        panel.add(tipoTarjetaComboBox);
        panel.add(new JLabel("Minuto:"));
        panel.add(minutoField);
        panel.add(new JLabel("Motivo:"));
        panel.add(motivoField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Tarjeta para " + selectedPartido.toString(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Jugador jugador = (Jugador) jugadorComboBox.getSelectedItem();
                String tipo = (String) tipoTarjetaComboBox.getSelectedItem();
                int minuto = Integer.parseInt(minutoField.getText().trim());
                String motivo = motivoField.getText().trim();

                if (jugador == null || tipo == null || minuto <= 0 || motivo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Datos inválidos para la tarjeta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                campeonato.registrarTarjeta(selectedPartido.getId(), jugador.getId(), tipo, minuto, motivo);
                JOptionPane.showMessageDialog(this, "Tarjeta registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                updatePartidoTable();
                updateAllStats(); // Notificar cambio en estadísticas
                dataManager.save(campeonato); // Guardar cambios
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El minuto debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al Registrar Tarjeta", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openRegistrarFaltaDialog() {
        Partido selectedPartido = getSelectedPartido();
        if (selectedPartido == null) return;

        List<Jugador> jugadoresEnPartido = new ArrayList<>();
        if (selectedPartido.getEquipoLocal() != null) {
            jugadoresEnPartido.addAll(selectedPartido.getEquipoLocal().getJugadores());
        }
        if (selectedPartido.getEquipoVisitante() != null) {
            jugadoresEnPartido.addAll(selectedPartido.getEquipoVisitante().getJugadores());
        }

        if (jugadoresEnPartido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay jugadores registrados para este partido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<Jugador> jugadorCometeComboBox = new JComboBox<>(jugadoresEnPartido.toArray(new Jugador[0]));
        JComboBox<Jugador> jugadorAfectadoComboBox = new JComboBox<>(jugadoresEnPartido.toArray(new Jugador[0]));
        JTextField minutoField = new JTextField(5);
        JTextField zonaCampoField = new JTextField(15);
        String[] tiposTarjeta = {"Ninguna", "Amarilla", "Roja"};
        JComboBox<String> tarjetaAsociadaComboBox = new JComboBox<>(tiposTarjeta);


        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Jugador que comete la falta:"));
        panel.add(jugadorCometeComboBox);
        panel.add(new JLabel("Jugador afectado:"));
        panel.add(jugadorAfectadoComboBox);
        panel.add(new JLabel("Minuto:"));
        panel.add(minutoField);
        panel.add(new JLabel("Zona del Campo:"));
        panel.add(zonaCampoField);
        panel.add(new JLabel("Tarjeta Asociada:"));
        panel.add(tarjetaAsociadaComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Falta para " + selectedPartido.toString(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Jugador jugadorComete = (Jugador) jugadorCometeComboBox.getSelectedItem();
                Jugador jugadorAfectado = (Jugador) jugadorAfectadoComboBox.getSelectedItem();
                int minuto = Integer.parseInt(minutoField.getText().trim());
                String zonaCampo = zonaCampoField.getText().trim();
                String tarjetaAsociada = (String) tarjetaAsociadaComboBox.getSelectedItem();
                if ("Ninguna".equals(tarjetaAsociada)) {
                    tarjetaAsociada = null; // Si es "Ninguna", se guarda como null
                }

                if (jugadorComete == null || jugadorAfectado == null || minuto <= 0 || zonaCampo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Datos inválidos para la falta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                campeonato.registrarFalta(selectedPartido.getId(), jugadorComete.getId(), jugadorAfectado.getId(), minuto, zonaCampo, tarjetaAsociada);
                JOptionPane.showMessageDialog(this, "Falta registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                updatePartidoTable();
                updateAllStats(); // Notificar cambio en estadísticas
                dataManager.save(campeonato); // Guardar cambios
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El minuto debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al Registrar Falta", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- Creación y Lógica del Panel de Estadísticas ---
    private JPanel createEstadisticasPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane statsTabbedPane = new JTabbedPane();

        // --- Pestaña de Tabla de Posiciones ---
        JPanel tablaPosicionesPanel = new JPanel(new BorderLayout(5, 5));
        tablaPosicionesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] tablaPosicionesColumnNames = {"Equipo", "Puntos", "Ganados", "Empatados", "Perdidos", "Goles Favor", "Goles Contra", "Tarjetas", "Faltas"};
        tablaPosicionesModel = new DefaultTableModel(tablaPosicionesColumnNames, 0);
        tablaPosicionesTable = new JTable(tablaPosicionesModel);
        tablaPosicionesTable.setFillsViewportHeight(true);
        tablaPosicionesPanel.add(new JScrollPane(tablaPosicionesTable), BorderLayout.CENTER);
        JButton refreshTablaButton = new JButton("Actualizar Tabla de Posiciones");
        refreshTablaButton.addActionListener(e -> updateTablaPosiciones());
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshPanel.add(refreshTablaButton);
        tablaPosicionesPanel.add(refreshPanel, BorderLayout.SOUTH);
        statsTabbedPane.addTab("Tabla de Posiciones", tablaPosicionesPanel);

        // --- Pestaña de Estadísticas de Equipo ---
        JPanel equipoStatsPanel = new JPanel(new BorderLayout(5, 5));
        equipoStatsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel equipoSelectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        estadisticasEquipoComboBox = new JComboBox<>();
        estadisticasEquipoComboBox.addActionListener(e -> displayEquipoStats());
        equipoSelectorPanel.add(new JLabel("Seleccionar Equipo:"));
        equipoSelectorPanel.add(estadisticasEquipoComboBox);
        equipoStatsPanel.add(equipoSelectorPanel, BorderLayout.NORTH);

        JPanel statsDisplayPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        statsDisplayPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas del Equipo"));
        partidosGanadosLabel = new JLabel("Ganados: ");
        partidosEmpatadosLabel = new JLabel("Empatados: ");
        partidosPerdidosLabel = new JLabel("Perdidos: ");
        golesAFavorLabel = new JLabel("Goles a Favor: ");
        golesEnContraLabel = new JLabel("Goles en Contra: ");
        puntosLabel = new JLabel("Puntos: ");
        totalTarjetasLabel = new JLabel("Total Tarjetas: ");
        totalFaltasLabel = new JLabel("Total Faltas: ");

        statsDisplayPanel.add(new JLabel("Partidos Ganados:"));
        statsDisplayPanel.add(partidosGanadosLabel);
        statsDisplayPanel.add(new JLabel("Partidos Empatados:"));
        statsDisplayPanel.add(partidosEmpatadosLabel);
        statsDisplayPanel.add(new JLabel("Partidos Perdidos:"));
        statsDisplayPanel.add(partidosPerdidosLabel);
        statsDisplayPanel.add(new JLabel("Goles a Favor:"));
        statsDisplayPanel.add(golesAFavorLabel);
        statsDisplayPanel.add(new JLabel("Goles en Contra:"));
        statsDisplayPanel.add(golesEnContraLabel);
        statsDisplayPanel.add(new JLabel("Puntos Totales:"));
        statsDisplayPanel.add(puntosLabel);
        statsDisplayPanel.add(new JLabel("Total Tarjetas:"));
        statsDisplayPanel.add(totalTarjetasLabel);
        statsDisplayPanel.add(new JLabel("Total Faltas:"));
        statsDisplayPanel.add(totalFaltasLabel);

        equipoStatsPanel.add(statsDisplayPanel, BorderLayout.CENTER);
        statsTabbedPane.addTab("Estadísticas de Equipo", equipoStatsPanel);

        // --- Pestaña de Estadísticas de Jugador ---
        JPanel jugadorStatsPanel = new JPanel(new BorderLayout(5, 5));
        jugadorStatsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel jugadorSelectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        estadisticasJugadorComboBox = new JComboBox<>();
        estadisticasJugadorComboBox.addActionListener(e -> displayJugadorStats());
        jugadorSelectorPanel.add(new JLabel("Seleccionar Jugador:"));
        jugadorSelectorPanel.add(estadisticasJugadorComboBox);
        jugadorStatsPanel.add(jugadorSelectorPanel, BorderLayout.NORTH);

        JPanel jugadorStatsDisplayPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        jugadorStatsDisplayPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas del Jugador"));
        golesJugadorLabel = new JLabel("Goles: ");
        tarjetasJugadorLabel = new JLabel("Tarjetas: ");
        faltasJugadorLabel = new JLabel("Faltas: ");
        partidosJugadosJugadorLabel = new JLabel("Partidos Jugados: ");

        jugadorStatsDisplayPanel.add(new JLabel("Goles Anotados:"));
        jugadorStatsDisplayPanel.add(golesJugadorLabel);
        jugadorStatsDisplayPanel.add(new JLabel("Tarjetas Recibidas:"));
        jugadorStatsDisplayPanel.add(tarjetasJugadorLabel);
        jugadorStatsDisplayPanel.add(new JLabel("Faltas Cometidas:"));
        jugadorStatsDisplayPanel.add(faltasJugadorLabel);
        jugadorStatsDisplayPanel.add(new JLabel("Partidos Jugados:"));
        jugadorStatsDisplayPanel.add(partidosJugadosJugadorLabel);

        jugadorStatsPanel.add(jugadorStatsDisplayPanel, BorderLayout.CENTER);
        statsTabbedPane.addTab("Estadísticas de Jugador", jugadorStatsPanel);


        panel.add(statsTabbedPane, BorderLayout.CENTER);

        return panel;
    }

    private void updateEstadisticasEquipoComboBox() {
        estadisticasEquipoComboBox.removeAllItems();
        for (Equipo equipo : campeonato.getEquipos()) {
            estadisticasEquipoComboBox.addItem(equipo);
        }
        displayEquipoStats();
    }

    private void updateEstadisticasJugadorComboBox() {
        estadisticasJugadorComboBox.removeAllItems();
        // Recopilar todos los jugadores de todos los equipos
        List<Jugador> allPlayers = new ArrayList<>();
        for (Equipo equipo : campeonato.getEquipos()) {
            if (equipo != null && equipo.getJugadores() != null) { // Added null checks
                allPlayers.addAll(equipo.getJugadores());
            }
        }
        // Ordenar jugadores por nombre para una mejor visualización
        allPlayers.sort(Comparator.comparing(Jugador::getNombreCompleto, Comparator.nullsLast(String::compareTo))); // Handle null names

        for (Jugador jugador : allPlayers) {
            estadisticasJugadorComboBox.addItem(jugador);
        }
        displayJugadorStats();
    }

    private void updateTablaPosiciones() {
        tablaPosicionesModel.setRowCount(0); // Limpiar la tabla
        List<Object[]> tabla = campeonato.getTablaDePosiciones();
        for (Object[] row : tabla) {
            tablaPosicionesModel.addRow(row);
        }
    }

    private void displayEquipoStats() {
        Equipo selectedEquipo = (Equipo) estadisticasEquipoComboBox.getSelectedItem();
        if (selectedEquipo != null) {
            partidosGanadosLabel.setText(String.valueOf(selectedEquipo.calcularPartidosGanados()));
            partidosEmpatadosLabel.setText(String.valueOf(selectedEquipo.calculaPartidosEmpatados()));
            partidosPerdidosLabel.setText(String.valueOf(selectedEquipo.calculaPartidosPerdidos()));
            golesAFavorLabel.setText(String.valueOf(selectedEquipo.calculaGolesAFavor()));
            golesEnContraLabel.setText(String.valueOf(selectedEquipo.calculaGolesEnContra()));
            puntosLabel.setText(String.valueOf(selectedEquipo.calculaPuntos()));
            totalTarjetasLabel.setText(String.valueOf(selectedEquipo.calculaTotalTarjetas()));
            totalFaltasLabel.setText(String.valueOf(selectedEquipo.contarFaltas()));
        } else {
            partidosGanadosLabel.setText("N/A");
            partidosEmpatadosLabel.setText("N/A");
            partidosPerdidosLabel.setText("N/A");
            golesAFavorLabel.setText("N/A");
            golesEnContraLabel.setText("N/A");
            puntosLabel.setText("N/A");
            totalTarjetasLabel.setText("N/A");
            totalFaltasLabel.setText("N/A");
        }
    }

    private void displayJugadorStats() {
        Jugador selectedJugador = (Jugador) estadisticasJugadorComboBox.getSelectedItem();
        if (selectedJugador != null) {
            golesJugadorLabel.setText(String.valueOf(campeonato.calculaGolesJugador(selectedJugador.getId())));
            tarjetasJugadorLabel.setText(String.valueOf(campeonato.calculaTarjetasJugador(selectedJugador.getId())));
            faltasJugadorLabel.setText(String.valueOf(campeonato.calculaFaltasJugador(selectedJugador.getId())));
            partidosJugadosJugadorLabel.setText(String.valueOf(campeonato.calculaPartidosJugadosJugador(selectedJugador.getId())));
        } else {
            golesJugadorLabel.setText("N/A");
            tarjetasJugadorLabel.setText("N/A");
            faltasJugadorLabel.setText("N/A");
            partidosJugadosJugadorLabel.setText("N/A");
        }
    }

    private void updateAllStats() {
        updateTablaPosiciones();
        updateEstadisticasEquipoComboBox();
        updateEstadisticasJugadorComboBox();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CampeonatoApp::new);
    }
}
