package Dal;

import Model.Guitar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class GuitarData {
    // For reading guitar objects from a .dat file

    //writing extra guitars to the list
    private void writeGuitars(ObservableList<Guitar> guitars) {
        try (FileOutputStream fos = new FileOutputStream("src/Resources/Files/guitars.dat", true);
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            for (Guitar guitar : guitars) {
                oos.writeObject(guitar);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //for refreshing the guitar documents
    private void overWriteGuitars(ObservableList<Guitar> guitars) {
        try (FileOutputStream fos = new FileOutputStream("src/Resources/Files/guitars.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            for (Guitar guitar : guitars) {
                oos.writeObject(guitar);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  // for writing only one guitar not used at the moment.

    private void writeGuitar(Guitar guitar) {
        try (FileOutputStream fos = new FileOutputStream("src/Resources/Files/guitars.dat", true);
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(guitar);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //for reading guitars
    public ArrayList<Guitar> readGuitars() {
        ArrayList<Guitar> guitars = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("src/Resources/Files/guitars.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    Guitar guitar = (Guitar) ois.readObject();
                    guitars.add(guitar);
                } catch (EOFException eofe) { // Inspired from the lecture
                    break;
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return guitars;
    }


    //if boolean is true subtract, otherwise add. this method updates the stock of the chosen guitars by user.
    public void updateStockData(ObservableList<ObservableMap.Entry<Guitar, Integer>> guitars, boolean isSubtract) {
        ArrayList<Guitar> oldGuitarList = readGuitars();
        ArrayList<Guitar> newGuitarList = new ArrayList<>();
        for (Map.Entry<Guitar, Integer> entry : guitars) {
            if (isGuitarInList(oldGuitarList, entry.getKey())) {

                if (isSubtract) {
                    entry.getKey().setStock(entry.getKey().getStock() - entry.getValue());
                } else {
                    entry.getKey().setStock(entry.getKey().getStock() + entry.getValue());
                }
                newGuitarList.add(entry.getKey());
            }
        }
        for (Guitar g : oldGuitarList) {
            if (!isGuitarInList(newGuitarList, g)) {
                newGuitarList.add(g);
            }
        }
        overWriteGuitars(FXCollections.observableArrayList(newGuitarList));
    }

//Check if guitar is in list
    boolean isGuitarInList(ArrayList<Guitar> allGuitars, Guitar guitar) {
        for (Guitar g : allGuitars) {
            if (g.getModel().equals(guitar.getModel()))
                return true;
        }
        return false;
    }
}



