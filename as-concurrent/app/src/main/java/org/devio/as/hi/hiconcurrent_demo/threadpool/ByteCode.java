package org.devio.as.hi.hiconcurrent_demo.threadpool;

public class ByteCode {
    public static void main(String[] args) {
        int capacity = (1 << 29) - 1;
        int running = -1 << 29;
        int shutdown = 0 << 29;
        int stop = 1 << 29;
        int tiding = 2 << 29;
        int terminal = 3 << 29;

        String bsCapacity = Integer.toBinaryString(capacity);
        String bsRunning = Integer.toBinaryString(running);
        String bsShutdown = Integer.toBinaryString(shutdown);
        String bsStop = Integer.toBinaryString(stop);
        String bsTiding = Integer.toBinaryString(tiding);
        String bsTerminal = Integer.toBinaryString(terminal);

        int ctl = running | 1;
        String bsCtl = Integer.toBinaryString(ctl);


        System.out.println("running    ：" + bsRunning);
        System.out.println("bsShutdown ：" + bsShutdown);
        System.out.println("stop       ：" + "00" + bsStop);
        System.out.println("tiding     ：" + "0" + bsTiding);
        System.out.println("terminal   ：" + "0" + bsTerminal);

        System.out.println("capacity   ：" + "000" + bsCapacity);
        System.out.println("bsCtl      ：" + bsCtl);

        //c & CAPACITY
        int workCount = ctl & capacity;
        System.out.println("workCount  ：" + workCount);

        //c & ~CAPACITYExecutors
        int state = ctl & ~capacity;
        System.out.println("state      ：" + Integer.toBinaryString(state));
    }
}
