package graphic;



import Model.Zamek;
import Pamatky.Pamatky;
import Table.TableException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import util.eTypKey;
import util.eTypProhl;


import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Optional;

public class MainFX extends Application {

    private Pamatky pamatky;
    private ListView<Zamek> langsListView;
    private TextField field;
    private eTypProhl typProhl;


    public static void main(String[] args) {
        launch(args);
    }


        private ControlPanelVbox comandsV(){
            var controlV = new ControlPanelVbox();
            controlV.newButton("FIRST", nastavPrvni());
            controlV.newButton("NEXT", nastavDalsi());
            controlV.newButton("PREVIOUS", nastavPred());
            controlV.newButton("LAST", nastavPosledni());
            controlV.newButton("DELETE", vyjmi());
            controlV.newButton("NAJDI NEJBLIZ",najdiNejblizsi());
            controlV.newButton("NAJDI ZAMEK",najdi());
            controlV.newComboBox("ITERACE",typIteraci());
            return controlV;
        }

    private EventHandler<ActionEvent> typIteraci() {
        return actionEvent -> {
          typProhl = ((ComboBox<eTypProhl>)actionEvent.getSource()).getValue();
          obnovList();
        };
    }

    private EventHandler<ActionEvent> nastavPred() {
        return actionEvent -> {
            langsListView.getSelectionModel().select(langsListView.getSelectionModel().getSelectedIndex()-1);
        };
    }

    private EventHandler<ActionEvent> najdi() {
        return actionEvent -> {
            try {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("NADJDI ZAMEK");
                dialog.setHeaderText("Zadejte nazev zamku");
                dialog.setContentText("Nazev:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    String klic = result.get();
                    Zamek zamek = pamatky.najdiZamek(klic);
                    langsListView.getSelectionModel().select(zamek);
                    langsListView.scrollTo(zamek);
                }
            }catch (TableException e){
                alert(e.getMessage());
            }
        };
    }

    private EventHandler<ActionEvent> najdiNejblizsi() {
        return actionEvent -> {
            try {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("NADJDI NEJBLIZSI ZAMEK");
                dialog.setHeaderText("Zadejte GPS(GPS musi byt ve spravnem formatu)");
                dialog.setContentText("GPS:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    Zamek zamek = pamatky.najdiNejbliz(result.get());
                    System.out.println(zamek);
                    if (zamek != null) {
                        langsListView.getSelectionModel().select(zamek);
                        langsListView.scrollTo(zamek);
                    } else alert("Spatny format souřadnicu");
                }
            }catch (TableException e){
                alert(e.getMessage());
            }
        };
    }


    private ControlPanelHBox commandsH() {
            var controlH = new ControlPanelHBox();
            controlH.newLabel("");
            controlH.newButton("IMPORT", nacist());
            controlH.newButton("CLEAR ALL", zrusit());
//            controlH.newLabel("NEW DATA:");
            controlH.newButton("Novy Zamek",novyA());
            controlH.newButton("PREBUDUJ", prebuduj());
            controlH.newLabel("TYP KLICU: ");
            field = controlH.newField(pamatky.getActualKlic().name());
//            controlH.newComboBoxPozice("position",pozice());
            return controlH;
        }


    private EventHandler<ActionEvent> prebuduj() {
        return actionEvent -> {
            try {
                if(pamatky.getActualKlic()==eTypKey.GPS){
                    pamatky.nastavKlic(eTypKey.NAZEV);
                } else pamatky.nastavKlic(eTypKey.GPS);
                pamatky.prebuduj();
                field.setText(pamatky.getActualKlic().name());
                obnovList();
            } catch (TableException e) {
                e.printStackTrace();
            }

        };
    }


    @Override
        public void start(Stage stage) throws Exception {
            pamatky = new Pamatky();
            typProhl = eTypProhl.HLOUBKA;
            FlowPane root = new FlowPane(creatList(),comandsV(),commandsH());
            Scene scene = new Scene(root, 620,590);
            stage.setScene(scene);
            stage.setMaxWidth(640);
            stage.setMaxHeight(620);
            stage.setResizable(false);
            stage.setTitle("Zamky");
            stage.show();
    }


    private EventHandler<ActionEvent> novyA() {
        return event ->{
            try {
                ProcesDialog procesDialog = new ProcesDialog(new Zamek("", "", null,pamatky.getActualKlic()));
                procesDialog.showAndWait();
                Zamek zamek = procesDialog.vratit();
                procesDialog.close();
                if (zamek != null) {
                    pamatky.vlozZamek(zamek);
                    obnovList();
                    System.out.println(zamek);
                }
            } catch (TableException e) {
                alert(e.getMessage());
            }

        };
    }

    private void obnovList() {
        try {
            Iterator<Zamek> iterator = pamatky.vytvorIterator(eTypProhl.HLOUBKA);
            langsListView.getItems().clear();
            switch (typProhl){
                case SIRKA -> iterator = pamatky.vytvorIterator(eTypProhl.SIRKA);
            }
            while (iterator.hasNext()) {
                langsListView.getItems().add(iterator.next());
            }
        } catch (TableException e){ alert(e.getMessage());}
    }
    private void obnovList(Zamek zamek){
        try {
            langsListView.getItems().clear();
            Iterator<Zamek> iterator = pamatky.vytvorIterator(eTypProhl.HLOUBKA);
            while (iterator.hasNext()) {
                langsListView.getItems().add(iterator.next());
            }
            langsListView.getSelectionModel().select(zamek);
        }catch (TableException e){ alert(e.getMessage());}
        }

    private EventHandler<ActionEvent> zrusit() {
        return event ->{
            langsListView.getSelectionModel().select(null);
            pamatky.zrus();
            langsListView.getItems().clear();
        };

    }

    private EventHandler<ActionEvent> nacist() {
        return actionEvent -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Soubor");
            dialog.setHeaderText("Zadejte nazev souboru");
            dialog.setContentText("Nazev:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                int i = 0;
                try {
                    i = pamatky.importDatZTXT(result.get());
                } catch (FileNotFoundException | TableException e) {
                    alert(e.getMessage());
                }
                if(i>0)obnovList();
                else new Alert(Alert.AlertType.INFORMATION,"Chyba cteni dat",ButtonType.OK).show();
            }
        };

    }


    private EventHandler<ActionEvent> vyjmi() {
        return event -> {
            try {
                switch (pamatky.getActualKlic()) {
                    case NAZEV -> pamatky.odeberZamek(langsListView.getSelectionModel().getSelectedItem().getNazev());
                    case GPS -> pamatky.odeberZamek(langsListView.getSelectionModel().getSelectedItem().getGps().toString());
                }
                obnovList();
            }catch (TableException e) {alert(e.getMessage());}
        };
    }

    private EventHandler<ActionEvent> nastavPosledni() {
        return event -> {
            langsListView.getSelectionModel().selectLast();
            langsListView.scrollTo(langsListView.getSelectionModel().getSelectedItem());
        };
    }

    private EventHandler<ActionEvent> nastavDalsi() {
        return event ->{
            langsListView.getSelectionModel().selectNext();
        };
    }


    private EventHandler<ActionEvent> nastavPrvni() {
       return event -> {
           langsListView.getSelectionModel().selectFirst();
           langsListView.scrollTo(langsListView.getSelectionModel().getSelectedItem());
       };
    }


    private ListView<Zamek> creatList(){
        ObservableList<Zamek> langs = FXCollections.observableArrayList();
        langsListView = new ListView<>(langs);
        langsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        langsListView.setPrefSize(500, 500);
        return langsListView;
    }

    private void alert(String msg){
        new Alert(Alert.AlertType.INFORMATION,msg,ButtonType.OK).show();
    }

}
