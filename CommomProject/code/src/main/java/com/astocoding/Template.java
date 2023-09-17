package com.astocoding;

public class Template {
    public static void main(String[] args) {
        new Template().runAlg(new TemA());
        new Template().runAlg(new TemB());
    }

    public void runAlg(Tem tem){
        tem.setp1();
        tem.setp2();
        tem.setp3();
    }
}

interface Tem{
    void setp1();
    void setp2();
    void setp3();
}

class TemA implements Tem{

    @Override
    public void setp1() {
        System.out.println("template A method 1");
    }

    @Override
    public void setp2() {
        System.out.println("template A method 2");
    }

    @Override
    public void setp3() {
        System.out.println("template A method 3");
    }
}


class TemB implements Tem{

    @Override
    public void setp1() {
        System.out.println("template B method 1");
    }

    @Override
    public void setp2() {
        System.out.println("template B method 2");
    }

    @Override
    public void setp3() {
        System.out.println("template B method 3");
    }
}
