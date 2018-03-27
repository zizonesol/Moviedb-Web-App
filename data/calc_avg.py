



if __name__ == "__main__":

    c = 0

    qavg = 0

    exavg = 0

    while True:

        user = input("type in file name or type in q to calculate\n")
        if(user == "q"):
            
            break
        try:
            with open("2_4p1.txt") as file:
                for line in file:
                    sp = line[:-2].split(" ");
                    qavg += float(sp[1])
                    exavg += float(sp[0])
                    c+=1
        except:
            print("invalid file")
    print("Total line calculated: " + str(c))
    print("Execution average: " + str(exavg/c))
    print("Query average: " + str(qavg/c))
    
