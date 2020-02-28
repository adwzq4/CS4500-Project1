"""
DESCRIPTION:
This is a group project for Introduction to the Software Profession
course at UMSL under Dr. Keith Miller.
This program implements an exhaustive or a "brute force" version
of the famous algorithm, the Traveling Salesman Problem (TSP). 
CONTRIBUTORS: James Brown (jtb9d2@mail.umsl.edu)
              Safiullah Khan (sskkn7@mail.umsl.edu)
              Eva Roeder (emrf2b@mail.umsl.edu)
              Adam Wilson (adwzq4@gmail.com)
PROJECT NAME: "Implementation of an Exhaustive Traveling Salesman Problem"
CLASS: Cmp Sci 4500-02
FILENAME: CS4500_TSP_Group10.py
EXTERNAL FILES: N/A
EXTERNAL FILES CREATED: N/A
DATE FINISHED: 02/27/2020
DATE SUBMITTED: 02/28/2020
RESOURCES: 
https://stackoverflow.com/questions/240178/list-of-lists-changes-reflected-across-sublists-unexpectedly
https://stackoverflow.com/questions/11858159/displaying-python-2d-list-without-commas-brackets-etc-and-newline-after-every
"""
import collections #for ordered dictionary
import math #for square root
from random import seed
from random import randint
from itertools import permutations #creating city routes
seed(4500)

#method that takes an integer from a user that 
#will be used as the number of cities the salesman
#visits
def user_pick_num_cities():
    k = int(input("Please enter the number of cities the salesman will visit. \n" +
                  "Entries must be between 4 nd 9, inclusive. \n"))
    while(k < 4 or k > 9):
        print("Only enter an integer value of 4, 5, 6, 7, 8, or 9:")
        k = int(input("Please re-enter a value for k: \n"))
    return k
  
#method that takes an integer from a user that
#determines the length of one side of the "map"
def user_pick_grid_len():
    n = int(input("Please enter the length of one side of the grid the salesman \n"+
                  "will be traveling on. \n"+
                  "Entries must be between 10 and 30, inclusive. \n"))
    while(n < 10 or n > 30):
        print("Only enter an integer value between 10 and 30, including 10 and 30:")
        n = int(input("Please re-enter a value for n: \n"))
    return n
  
#method that creates semi-randomly generated
#pairs of numbers/coordinates to be used as 
#city locations
def get_coordinates(k, n):
    city_coords = [] #list to hold coordinates
    
    for cities in range(k):          
        x, y = randint(0, n), randint(0, n)
        city_coords.append((x, y))
    
    print("\nCOORDINATES")
    print(*city_coords)
    return city_coords
  
#method that creates and prints a grid of size nxn
#and fills it with periods until the nested loop
#uses the coordinates created above to map 
#the city's locations
def create_grid(city, k, n):
    rows, cols = (n, n)
    grid = [['.']*cols for _ in range(rows)] 
    
    for i in range(k):
       for row, col in city:       
           if (row, col) != city[i]:
               continue
           else:
                grid[row-1][col-1] = i
                continue

    print("\nGRID")
    for row in grid:
        print (" ".join(map(str,row)))
        
#method that uses the coordinates to make and print 
#two matrices full of the distances to and from each
#of the cities. the x's and y's of each of the 
#coordinates are delineated and a dictionary of
#key, value pairs is created for ease of distinguishing
#each of the cities and their distances in the nested
#loop. one matrix tracks actual distances and one is 
#formatted to 2 decimal places for printing. 
def find_distances(coords, k):
    rows, cols = (k, k)
    dist_matrix = [[99]*cols for _ in range(rows)]
    matrix = [[99]*cols for _ in range(rows)]
    
    list_x = []
    list_y = []
    for x, y in coords:
        list_x.append(x)
        list_y.append(y)
    
    Dict_of_coords = collections.OrderedDict()
    [Dict_of_coords.setdefault(i, coords[i]) for i in range(0, len(coords))]
    
    for key, values in Dict_of_coords.items():
        for i in range(k):
            if matrix[key][i] == 99:
                num = math.sqrt( (list_x[key] - list_x[i])**2 + (list_y[key] - list_y[i])**2 )
                dist_matrix[key][i] = math.sqrt( (list_x[key] - list_x[i])**2 + (list_y[key] - list_y[i])**2 )
                matrix[key][i] = "{0:.2f}".format(num)
            else:
                continue
    
    print("\nDISTANCE MATRIX")
    for row in matrix:
        print ("\t".join(map(str,row)) + "\n\n")          
        
    return dist_matrix

#method that calculates the shortest route
#given the distance matrix and route permutations
def calc_shortest_route(d_mat, k, n):
    #numbers to make the route permutations 
    numbers = []    
    for i in range(1, k):
        numbers.append(i)
    #permutations creates a tuple of numbers
    #the following puts the tuple into a list
    #so it can be manipulated            
    perms = permutations(numbers)
    perm_list = []
    for i in list(perms):
        perm_list.append(list(i))
    #the combinations need to have city 0
    #added to the beginning and end of each
    #permutation
    combos = []
    for i in perm_list:
        i.append(0)
        i.insert(0, 0)
        combos.append(i)
    
    temp = -1 #placeholder for counter
    counter = 0#stores the index with the lowest distance
    shortest = 10000#stores current shortest
    #for each of the combos, temp is increased by one, 
    #temp_dist is reset to calculate the next route's 
    #distance, and the current distance is compared to
    #current shortest distance and the optimal route 
    #is determined
    for combo in combos:
        temp += 1
        temp_dist = 0
        for i in range(k):
            temp_dist += d_mat[combo[i]][combo[i+1]]
            
        if temp_dist < shortest:
            shortest = temp_dist
            counter = temp
            print("The shortest route so far, ")
            print(combos[counter])
            print("has a distance of ")
            print(shortest)     
    
    optimal_route = combos[counter]
    
    #print best path and distance to the user
    print("\nOPTIMAL ROUTE")
    print("The shortest route is ") 
    print(optimal_route)
    print("with a distance of ")
    print(shortest)
    
############### MAIN ################
#picking the cities and grid length
k = user_pick_num_cities()
n = user_pick_grid_len()

#get the coordinates
city_coords = get_coordinates(k, n)

#show the cities on a grid
create_grid(city_coords, k, n)

#get distances
dist_matrix = find_distances(city_coords, k)

#calculate the routes and find shortest
calc_shortest_route(dist_matrix, k, n)
