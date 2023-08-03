import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private static User user;
    private JTextField tfEmail;
    private JTextField tfContaseña;
    private JButton inicarSesionButton;
    private JPanel loginPanel;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("login");
        setContentPane(loginPanel);
        setMaximumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        inicarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email =tfEmail.getText();
                String contraseña = String.valueOf(tfContaseña.getText());

                Object user = getAuthenticatedUSer(email, contraseña);

                if (user !=null);{
                    dispose();
                }
                 {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "email o contraseña inavlidos",
                            "try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public User getAuthenticatedUSer(String email, String contraseña){
        User user = null;
        final String DB_URL = "jdbc:sqlserver://localhost:1433;";
        final String USERNAME = "sa";
        final String PASSWORD = "";

        try{
        Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password";
            PreparedStatement preparedStatement =con.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, contraseña);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                user = new User ();
                user.Email = email;
                user.Contraseña =contraseña;
            }

            stmt.close();
            con.close();

        }catch(Exception e){
            e.printStackTrace();
        }


        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = LoginForm.user;
        if (user !=null){
            System.out.println("autenticacion valida de: "+ user.Nombre);
            System.out.println("        email:" + user.Email);
            System.out.println("        apellido" + user.Apellido);

    }
        else {
            System.out.println("autenticacion invalida");
        }
    }
}
