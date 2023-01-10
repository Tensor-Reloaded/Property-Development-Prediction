import matplotlib.pyplot as plt

list_batches = [x for x in range(100,320,20)]

min_data = []
max_data = []
avg_data = []

def read_data_from_file(batch_size, offset_percentage):
    list_times = []
    offset = int(batch_size*offset_percentage/100)
    if offset == 0:
        offset = 1
    with open(str(batch_size) + ".txt", "r") as reader:
        list_read = reader.read().split(" ")
        max_data.append(int(list_read[0]))
        min_data.append(int(list_read[1]))
        avg_data.append(int(list_read[2]))

for batch_size in list_batches:
    read_data_from_file(batch_size=batch_size, offset_percentage=1)

plt.plot(list_batches,max_data,marker="o",linestyle="--")
plt.xticks(list_batches,list_batches)
plt.xticks(rotation=90)
plt.yticks(max_data,max_data)
plt.legend(handletextpad=0.1)
plt.ylabel('Times (milliseconds)')
plt.title("Requests count - Maximum")
plt.gcf().autofmt_xdate()
plt.show()


plt.plot(list_batches,min_data,marker="o",linestyle="--")
plt.xticks(list_batches,list_batches)
plt.xticks(rotation=90)
plt.yticks(min_data,min_data)
plt.ylabel('Times (milliseconds)')
plt.title("Requests count - Minimum")
plt.show()


plt.plot(list_batches,avg_data,marker="o",linestyle="--")
plt.xticks(list_batches,list_batches)
plt.xticks(rotation=90)
plt.yticks(avg_data,avg_data)
plt.ylabel('Times (milliseconds)')
plt.title("Requests count - Average")
plt.show()