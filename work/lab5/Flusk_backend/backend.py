from flask import Flask, request

app = Flask(__name__)
app.config["DEBUG"] = True

@app.route('/api/predictProperty',methods=["GET","POST"])
def predictAPI():
    content = request.get_json()
    print(content)
    return predictImage(content['image'],content['years'])

def predictImage(image, years):
    # todo add here implementation for the model
    return image

app.run()