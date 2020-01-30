const express = require('express');
const app = express();

app.get('/api/cars', (req, res) => {
    res.send("Welcome to my API. Ready to book a car?");
});

app.get('/api/cars/:provider/:pickup_long&:pickup_lat/:dropoff_long&:dropoff_lat/:seats?', (req, res) => {
    let provider = req.params.provider;
    let pickup_long = req.params.pickup_long;
    let pickup_lat = req.params.pickup_lat;
    let dropoff_long = req.params.dropoff_long;
    let dropoff_lat = req.params.dropoff_lat;
    let seats = req.params.seats || 1;

    var exec = require('child_process').exec;
    var javaApp = 'java -jar cars-api.jar ' + provider + " " + pickup_long + " " + pickup_lat + " " + dropoff_long +
        " " + dropoff_lat + " " + seats;

    exec(javaApp, function (error, stdout, stderr) {
        res.status(200);
        let output = stdout.split("\n");

        let schema = {};
        schema["cars"] = [];

        for (let i = 0; i < output.length-1; i++) {
            let data = output[i].split("- ");
            schema["cars"].push({
                type: data[0].trim(),
                provider: data[1].trim(),
                price: data[2].trim()
            });
        }
    
        console.log(schema)
        res.send(schema); 
    });

});

module.exports = app; 