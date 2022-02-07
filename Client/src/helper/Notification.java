package helper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class Notification {

    private Image img=new Image("/Images/n3.png",40, 40, false, false);

    private String notificationBody;

    public  Notification(String notificationBody){
        this.notificationBody = notificationBody;
        show();
    }

    private void  show(){
        Notifications n = Notifications.create()
                .title("Notification")
                .text(notificationBody)
                .graphic(new ImageView(img))
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                ;
        n.darkStyle();
        n.show();
    }

}
