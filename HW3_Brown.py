"""
Travelling Salesperson
TBD
External Files: N/A
External Files Created: N/A
James Brown(jtb9d2@mail.umsl.edu), ....
Cmp Sci 4500-002
02/25/20
This program...
Used geeksforgeeks.com
"""

#Importing required libraries
import math 
import random

print("Hello there, this is a travelling salesperson simulatoin. " + 
"Please enter The nmber of cities(K) and size of the grid(N)" + 
" to see the shortest path...")

#Taking user input for the number of cities
K = int(input("Please enter a number of cities from 4 to 9: \n"))

while(int(K) > 9 or int(K) < 4): 
    K = int(input("K must be between 4 and 9 inclusively!\n" + 
    "Please re-enter an allowed value for K: \n"))

#taking user input for the size of the grid
N = int(input("Now, please enter a number fot the size of the grid from 10 to 30: \n"))

while(int(N) > 30 or int(N) < 10):
    N = int(input("N must be between 10 and 30 inclusively! \n" +
        "Please re-enter an allowed alue for N: \n"))

def salesPerson(K, N):
    
    cities = []

    grid = [[" * " for i in range(N)] for j in range(N)]
    for i in range(K):
        x = random.randrange(N)
        y = random.randrange(N)
        cities.append((x, y))
        grid[x][y] = " X "

    for i in range(len(grid)):
        for j in grid[i]: 
            print(j, end = "")
        print()

    minPath = 99999

    while True: 
        current = 0

        for i in range(K-1):
            current += math.sqrt(math.pow(cities[i+1][0] - cities[i][0], 2)\
            + math.pow(cities[i+1][1] - cities[i][1], 2))

        current += math.sqrt(math.pow(cities[K-1][0] - cities[i][0], 2)\
            + math.pow(cities[K-1][1] - cities[i][1], 2))

        if current < minPath:
            minPath = current
            print(cities)

        if not next_permutation(cities):
            break

    print(minPath)

def next_permutation(L):
    n = len(L)
    
    i = n - 2
    
    while i >= 0 and L[i] >= L[i + 1]:
        i -= 1

    if i == -1:
        return False
    
    j = i + 1

    while j < n and L[j] > L[i]:
        j += 1
    j -= 1

    L[i], L[j] = L[j], L[i]

    left = i + 1
    right = n - 1

    while left < right:
        L[left] = L[right]
        L[right] = L[left]
        left += 1
        right -= 1

    return True

salesPerson(K, N)