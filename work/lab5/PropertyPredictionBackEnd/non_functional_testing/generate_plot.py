import matplotlib.pyplot as plt

list_batches = [5, 10, 20, 40, 80, 160, 320]


def draw_plot(batch_size, offset_percentage):
    list_times = []
    offset = int(batch_size*offset_percentage/100)
    if offset == 0:
        offset = 1
    with open(str(batch_size) + ".txt", "r") as reader:
        list_read = reader.read().split(" ")
        list_read.pop()
        list_times = list_read
    print(list_times)
    plt.plot([i*offset for i in range(0, int(len(list_times)/offset))],
             [list_times[i*offset] for i in range(0, int(len(list_times)/offset))], "ro")
    plt.ylabel('Times (milliseconds)')
    plt.title("Requests count " + str(batch_size))
    plt.show()


for batch_size in list_batches:
    draw_plot(batch_size=batch_size, offset_percentage=7)
