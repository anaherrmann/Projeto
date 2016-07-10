package Lamport;

/* Copyright (c) 2012-2014. The SimGrid Team.
 * All rights reserved.                                                     */

/* This program is free software; you can redistribute it and/or modify it
 * under the terms of the license (GNU LGPL) which comes with this package. */
import java.util.ArrayList;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.VM;
//import org.simgrid.xbt.*;

/**
 * Example showing the use of the new experimental Cloud API.
 */
public class Cloud {

    public static final double task_comp_size = 10;
    public static final double task_comm_size = 10;
    public static final int hostNB = 3;

    public static void main(String[] args) throws MsgException {
        Msg.init(args);

        if (args.length < 1) {
            Msg.info("Usage	 : Cloud platform_file");
            Msg.info("Usage  : Cloud platLam.xml");
            System.exit(1);
        }
        
        /* Construct the platform */
        Msg.createEnvironment(args[0]);
        
        Host[] hosts = Host.all();
        if (hosts.length < hostNB + 1) {
            Msg.info("I need at least " + (hostNB + 1) + "  hosts in the platform file, but " + args[0] + " contains only " + hosts.length + " hosts");
            System.exit(42);
        }

        ArrayList<VM> vms = new ArrayList<>();
        ArrayList<Lamport> lnode = new ArrayList<>();

        for (int i = 0; i < hosts.length; i++) {
            VM vm = new VM(hosts[i], "VM0" + i);
            vm.start();
            vms.add(vm);
            Lamport n = new Lamport(vm, hosts[i].getName(), args);
            lnode.add(n);
        }
        
        Msg.deployApplication(args[1]);
        Msg.run();
        
    }
}