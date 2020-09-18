package demo.cycledepedency;

public class APerson {

    private BPerson bPerson;

    public APerson() {
        System.out.println("APerson 开始实例化,但是我需要依赖于BPerson");

    }

    public BPerson getbPerson() {
        return bPerson;
    }

    public void setbPerson(BPerson bPerson) {
        this.bPerson = bPerson;
    }
}
