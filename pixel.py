# -*- coding: utf-8 -*-
"""
Created on Thu Jul 14 19:07:44 2016

@author: Alfanhui
"""

import numpy as np
from drawnow import drawnow
import matplotlib.pyplot as plt
import time

class pixel():
    __slots__ = {'dimension'}

    def __init__(self):
        self.dimension = 400

    def update(self):
        plt.ion()
        fig = plt.figure()
        #plt.show(block=False)
        for k in range(1000):
            #self.draw_fig(outData)
            #plt.draw()
            drawnow(self.draw_fig)
            plt.pause(0.0001)

    def  draw_fig(self):
        #plt.subplot(1, 1, 1)
        outData = self.randomise()
        plt.imshow(outData, cmap=plt.cm.gray)


    def randomise(self):
        outData = np.random.randint(2, size=(self.dimension, self.dimension))
        return outData

    def permutate(self):
        #method to move items along in an array to reach all permutations.
        permValid = False; #boolean to check if permutation still should go on.
        while(!permValid): #start the array process.
            switch(rotation){
                case 0: #for first permutation, not used afterwards.
                    #System.out.println("CASE 0");

                sequence[0][0] = 1;
                displayArray();
                rotation++;
                break;
                case 1:
                System.out.println("CASE 1");
                for(int x=0;x<sequence.length;x++){
                    for(int y=0;y<sequence[x].length;y++){ //2nd rotation
                        try{
                            if(sequence[x][y] == 1){
                                sequence[x][y] = 0;
                                if(y==(di-1)){
                                    sequence[x+1][0] = 1;
                                }else
                                    sequence[x][y+1] = 1;
                                displayArray();
                                rotation++;
                                counter++;
                                break;
                            }
                        }catch(Throwable e){
                            System.out.println("CASE 1" + e);
                        }
                    }
                }
                case 2:
                System.out.println("CASE 2");
                for(int x=0;x<sequence.length;x++){
                    for(int y=0;y<sequence[x].length;y++){ //3rd rotation
                        try{
                            if(sequence[x][y] == 0){
                                sequence[x][y] = 1;
                                displayArray();
                                rotation++;
                                counter++;
                                break;
                            }
                        }catch(Throwable e){
                            System.out.println("CASE 2" + e);
                        }
                    }
                }
                case 3:
                System.out.println("CASE 3");
                for(int x=0;x<sequence.length;x++){
                    for(int y=0;y<sequence[x].length;y++){ //4rd rotation
                        try{
                            if(sequence[x][y] == 0){
                                sequence[x][y] = 1;
                                int b = y--;
                                for(int a = x;a > -1; a--){
                                    for(;b>-1;b--)
                                        sequence[a][b] = 0;
                                    b = (di-1);
                                }
                                displayArray();
                                rotation++;
                                counter++;
                                break;
                            }
                        }catch(Throwable e){
                            System.out.println("CASE 3" + e);
                        }
                    }
                }
                    case 4:
                System.out.println("CASE 4");
                    for(int x=0;x<sequence.length;x++){
                    for(int y=0;y<sequence[x].length;y++){ //3rd rotation
                        try{
                            if(sequence[x][y] == 0){
                                sequence[x][y] = 1;
                                displayArray();
                                rotation=1;
                                counter++;
                                break;
                            }
                        }catch(Throwable e){
                            System.out.println("CASE 4" + e);
                        }
                    }
                }

            }
            permValid = checkArray();
        }
        System.out.println("Total achieved Permutations = " + counter);
    }




    '''
    def start(self):
        #outData = np.zeros((self.dimension, self.dimension))
        f = 0
        i = 1000
        plt.ion()
        plt.figure()
        #ax = fig.add_subplot(111)
        while f < i:
            outData = self.randomise()
            plt.imshow(outData)
            #plt.imshow(outData)
            plt.draw()

    def start2(self):
        fig = plt.figure()
        for k in range(10):
            plt.clf()
            outData = self.randomise()
            plt.imshow(outData,cmap=plt.cm.gray)
            fig.canvas.draw()
            time.sleep(1e-60) #unnecessary, but useful
    '''










if __name__ == '__main__':
    pixel = pixel()
    pixel.update()
