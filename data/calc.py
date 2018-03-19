



if __name__ == "__main__":

    c = 0

    qavg = 0

    exavg = 0
    
    with open("2_4p1.txt") as file:
        for line in file:
            sp = line[:-2].split(" ");
            qavg += float(sp[1])
            exavg += float(sp[0])
            c+=1

    with open("2_4p2.txt") as file:
        for line in file:
            sp = line[:-2].split(" ");
            qavg += float(sp[1])
            exavg += float(sp[0])
            c+=1
    print(c)
    print(qavg/c)
    print(exavg/c)
