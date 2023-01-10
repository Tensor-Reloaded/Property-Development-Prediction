import threading
import requests
import time


class Coordinate:
    def __init__(self, latitude, longitute):
        self.latitude = latitude
        self.longitude = longitute


class Request:
    def __init__(self, image, years, coordinate):
        self.image = image
        self.coordinates = coordinate
        self.yearsInFuture = years


image = ""
list_times = []

with open("./image.txt", "r") as reader:
    image = reader.read()


def make_request(id_thread):
    global list_times
    url = "http://localhost:8080/api/predictDevelopment"
    # request = Request(image="----", years=4, coordinate=Coordinate(0, 0))
    # print(jsonpickle.encode(request,unpicklable=False))
    start = int(time.time()*1000)
    end = start
    try:
        # response = requests.post(url=url,json=jsonpickle.encode(request,unpicklable=False))
        response = requests.post(url=url, json={
            "coordinates": {
                "latitude": 0,
                "longitude": 0
            },
            "image": image,
            "yearsInFuture": 0
        })
        end = int(time.time()*1000)
        print(str(id_thread) + " response code: " + str(response.status_code) +
              " and time " + str(end - start) + " milliseconds")
    except:
        end = int(time.time()*1000)
        # print(str(id_thread)+" finished with error in " +
        #       str(end - start) + " milliseconds")
    list_times.append(end-start)


def write_data(batch_size):
    global list_times
    with open(str(batch_size)+".txt", "w") as writer:
        writer.write(str(max(list_times)) + " " + str(min(list_times)) + " " + str(int(sum(list_times)/len(list_times))))


def simultan_request(count_requests):
    global list_times
    list_times.clear()
    list = []

    for i in range(0, count_requests):
        thread = threading.Thread(target=make_request, args=(i,))
        thread.start()
        list.append(thread)

    for thread_item in list:
        thread_item.join()

    write_data(count_requests)

for x in range(100,320,20):
    simultan_request(x)