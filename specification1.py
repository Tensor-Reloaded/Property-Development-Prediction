from pythonrv import rv
import defs
import os

directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\Dataset_SpaceNet\\SN7_buildings_train\\train"
save_directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\train_augment"
patch_size = (128, 128)


@rv.monitor(function=defs.process_location)
@rv.spec(when=rv.POST)
def specification1(event):
    assert type(event.fn.function.result) is int
    print("1: The result of the \'defs.process_location\' function is of type int!")


@rv.spec(history_size=5)
@rv.monitor(created_directory=defs.create_directory, process_images=defs.process_images)
def specification2(event):
    if event.fn.process_images.called:
        count = len([old_event for old_event in event.history \
                     if old_event.called_function == old_event.fn.created_directory])
        assert count > 0
        print("2: Image processing finished with success!")


@rv.spec(when=rv.PRE)
@rv.monitor(function=defs.process_location)
def specification3(event):
    image_directory = event.fn.function.inputs[0]
    image_save_directory = event.fn.function.inputs[1]
    if type(event.fn.function.result) is not int:
        if image_save_directory == '' or image_directory == '' or image_directory is None or image_save_directory \
                is None:
            print("3: MOP: Using default read and save location!")
            function_args = list(event.fn.function.inputs)
            function_args = (
                "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\Dataset_SpaceNet\\SN7_buildings_train\\train",
                "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\train_augment",
                function_args[2], function_args[3], function_args[4])
            defs.process_location(function_args[0], function_args[1], function_args[2], function_args[3], function_args[4])
            print("3: Correction executed with success!")


directory = "E:\\Facultate\\_Master\\Advanced Software Engineering Techniques\\Dataset_SpaceNet\\SN7_buildings_train\\train"
save_directory = ""
patch_size = (128, 128)
processes_number = 5
augmentation_number = 1
work_portion_size = 60 // processes_number
keep_worker_alive = True

locations = os.listdir(directory)
locations = locations * augmentation_number

directory = ""
defs.process_location(directory, save_directory, (128, 128), locations[4], 1)
