package demo.cycledepedency;

public class BPerson {

    private CPerson bPerson;

    public BPerson() {
        System.out.println("BPerson 开始实例化,但是我需要依赖于CPerson");

    }

    public CPerson getbPerson() {
        return bPerson;
    }

    public void setbPerson(CPerson bPerson) {
        this.bPerson = bPerson;
    }
}
