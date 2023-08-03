import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegisterForm extends JDialog{

    private JTextField nombre;
    private JTextField Apellido;
    private JButton registrarseButton;
    private JLabel registrarse;
    private JTextField email;
    private JButton alumnoButton;
    private JButton maestroButton;
    private JTextField confirmarcOntraseña;
    private JTextField contraseña;
    private JPanel registerPanel;
    private JLabel nombreLabel;
    private JLabel apellidoLabel;
    private JLabel emailLabel;
    private JLabel cargoLabel;
    private JLabel contraseñaLabel;

    public RegisterForm(JFrame parent){
        super(parent);
        setTitle("crear una nueva cuenta");
        setContentPane(registerPanel);
        setMaximumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        registrarseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String nombre = this.nombre.getText();
        String apellido = Apellido.getText();
        String email = this.email.getText();
        String contraseña = String.valueOf(this.contraseña.getParent());
        String confirmarContraseña = String.valueOf(confirmarcOntraseña.getParent());

        if (nombre.isEmpty() || email.isEmpty() || apellido.isEmpty() || contraseña.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "por favor rellenar todos los espacios",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!contraseña.equals(confirmarContraseña)){
            JOptionPane.showMessageDialog(this,
                    "Confirmar contraseña",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(nombre,apellido,email,contraseña);
        if (user != null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "fallo al registrar el usuario",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;
    private User addUserToDatabase(String nombre, String apellido, String email, String contraseña) {
        User user = null;
        final String DB_URL = "jdbc:sqlserver://localhost:1433;";
        final String USERNAME = "sa";
        final String PASSWORD = "";

        try{
            Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement stmt = con.createStatement();
            String sql = "INSERT INTO users (nombre,apellido,email,contraseña )"+
                    "VALUES (?,?,?,?)";
            PreparedStatement preparedStatement =con.prepareStatement(sql);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, apellido);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, contraseña);

            int addedRows = preparedStatement.executeUpdate();
            if(addedRows > 0);{
                user = new User ();
                user.Nombre = nombre;
                user.Apellido = apellido;
                user.Email = email;
                user.Contraseña =contraseña;
            }

            stmt.close();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        RegisterForm myForm = new RegisterForm(null);
        User user = myForm.user;
        if (user !=null){
            System.out.println("completado el registro de: "+ user.Nombre);
        }
        else{
            System.out.println("no se completo el registro");
        }
    }
}
