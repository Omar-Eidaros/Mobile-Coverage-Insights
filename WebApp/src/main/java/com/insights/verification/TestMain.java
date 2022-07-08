package com.insights.verification;

public class TestMain {
    public static void main (String[] args){
        Verification verification = new Verification();
        System.out.println("before");
        System.out.println(verification.getVerifCode());
        System.out.println(verification.getId());
        verification.setVerifCode(verification.generateCode(5));
        verification.setId(Integer.parseInt(verification.generateCode(3)));
        System.out.println("after");
        System.out.println(verification.getVerifCode());
        System.out.println(verification.getId());

    }
}
