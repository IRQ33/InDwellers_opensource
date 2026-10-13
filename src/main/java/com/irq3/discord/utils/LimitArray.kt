package com.irq3.discord.utils

import java.util.stream.Collectors

class LimitArray<T> : ArrayList<T>{

    var maxsize: Int=0

    constructor(maxsize: Int):super(){
        this.maxsize=maxsize
    }

//    constructor(list: List<T>?):super(0){
//        if(list==null){
//            maxsize=0
//        }else{
//            addAll(list)
//            maxsize = list.size
//        }
//    }

    fun addElement(element:T){
        if (maxsize <= 0) {
            return
        }

        if (size < maxsize) {
            add(element)
        } else {
            for (i in 0 until size - 1) {
                set(i, get(i + 1))
            }
            set(size - 1, element)
        }
    }

    override fun toString(): String {
        return this.stream().map { it.toString() }.collect(Collectors.joining(",")) as String
    }
}