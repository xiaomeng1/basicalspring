package demo.cycledepedency;

public class CPerson {

    private APerson bPerson;

    public CPerson() {
        System.out.println("CPerson 开始实例化,但是我需要依赖于APerson");

    }

    public APerson getbPerson() {
        return bPerson;
    }

    public void setbPerson(APerson bPerson) {
        this.bPerson = bPerson;
    }
}


