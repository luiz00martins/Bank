package Bank;

import bankExceptions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Account implements Serializable {

    protected ID id;
    protected Agency agency;

    protected boolean exists(){
        return Files.exists(Paths.get("User Data/" + id + " " + agency));
    }

    protected Account() {
        this.id = null;
        this.agency = null;
    }

    public Account(ID id, Agency agency) {
        this.id = id;
        this.agency = agency;
    }

    public ID getId() {
        return id;
    }

    public Agency getAgency() {
        return agency;
    }

    public boolean equals(Account acc) {
        return (this.id.equals(acc.id) && this.agency.equals(acc.agency));
    }

}
