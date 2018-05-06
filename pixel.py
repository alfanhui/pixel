# -*- coding: utf-8 -*-
"""
Created on Thu Jul 14 19:07:44 2016

@author: Alfanhui
"""
import numpy as np
from drawnow import drawnow
import matplotlib.pyplot as plt
import time
import itertools
import math
import threading



class Pixel(threading.Thread):
    __slots__ = 'dimensions', 'outData', 'count', 'displayThread'

    def __init__(self):
        self.dimensions = 9
        self.outData = np.zeros((self.dimensions,self.dimensions), dtype=np.int)
        self.count = 1.0
        #plt.ion()
        #fig = plt.figure()
        threading.Thread.__init__(self)


    def draw_fig(self):
        #plt.subplot(1, 1, 1)
        #outData = self.randomise()
        plt.imshow(self.outData, cmap=plt.cm.gray)

    def randomise(self):
        self.outData = np.random.randint(2, size=(self.dimensions, self.dimensions))
        return self.outData

    def permutate0(self):
        #plt.ion()
        #fig = plt.figure()
        pixels = self.dimensions*self.dimensions
        for i in range(pixels,pixels+1):
            res = itertools.product([1,0], repeat=i)
            for j in res:
                self.outData = np.array(j).reshape(self.dimensions, self.dimensions)
                drawnow(self.draw_fig)
                #plt.pause(0.0001)

    def permutate1(self):
        #method to move items along in an array to reach all permutations.
        self.outData[0][0] = 1
        #self.update()
        #totalPerms = math.pow(2,(self.dimensions*self.dimensions))
        self.randomise()
        while(1):#(self.count < totalPerms): #start the array process.
            self.rotation1()
            self.rotation2()
            self.rotation3()
            self.rotation2()

    def rotation1(self):
        for x in xrange(len(--self.outData)):#2nd rotation
            for y in xrange(len(--self.outData[x])):
                if(self.outData[x][y] == 1):
                    self.outData[x][y] = 0
                    if(y == --self.dimensions):
                        self.outData[x+1][0] = 1
                    else:
                        self.outData[x][y+1] = 1
                    drawnow(self.draw_fig)
                    self.count +=1
                    return
    def rotation2(self):
        for x in xrange(len(--self.outData)):#3rd rotation
            for y in xrange(len(--self.outData[x])):
                if(self.outData[x][y] == 0):
                    self.outData[x][y] = 1
                    self.count +=1
                    drawnow(self.draw_fig)
                    return
    def rotation3(self):
        for x in xrange(len(--self.outData)):#4th rotation
            for y in xrange(len(--self.outData[x])):
                if(self.outData[x][y] == 0):
                    self.outData[x][y] = 1
                    b = (y-1)
                    a = x
                    for a in xrange(a, -1, -1):
                        for b in xrange(b, -1, -1):
                            self.outData[a][b] = 0
                        b = (self.dimensions-1)
                    self.count+=1
                    drawnow(self.draw_fig)
                    return

    def seqInterpretur(self):
        seqLength = float(math.pow(2,(self.dimensions*self.dimensions)))
        seqLower = 0.0
        seq = 0.0
        self.outData.resize(1)
        for x in range(len(self.outData)):
            if(seq == 0 and self.outData[x] == 1):
                seqLength-=1
                seq = math.pow(2,(seqLength-1))
            else:
                if(self.outData[x] == 1): #if 1 then sequence is higher than lowerbound.
                    seqLower -= 1
                    seqLower = math.power(2,(seqLength-1))
                    seq+=(seqLower/2)
               #else: #if 0 then lower bound is maximum.
            seqLength-=1
        print("Randomised sequence is: ", seq,  ".")
        print("Sequence is " , ((seq / math.pow(2,((self.dimensions*self.dimensions)-1)))*100),"%")


class Display(threading.Thread):
    def __init__(self):
        plt.ion()
        fig = plt.figure()
        threading.Thread.__init__(self)

    def run(self):
        while(1):
            drawnow(self.draw_fig)

    def draw_fig(self):
        #plt.subplot(1, 1, 1)
        #outData = self.randomise()
        plt.imshow(self.outData, cmap=plt.cm.gray)


if __name__ == '__main__':
    pixel = Pixel()
    #start = time.clock()
    #pixel.permutate0()
    #end = time.clock()
    #print(end - start)
    start = time.clock()
    pixel.permutate1()
    #pixel.displayThread.start()
    #pixel.start()
    #pixel.displayThread.join()
    #pixel.join()
    end = time.clock()
    print(end - start)
