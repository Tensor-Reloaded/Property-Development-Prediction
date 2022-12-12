import { Component, ElementRef, Injectable, OnInit, ViewChild } from '@angular/core';
import { GoogleMap } from '@angular/google-maps';
import { HttpClient } from '@angular/common/http';
import { DomSanitizer } from '@angular/platform-browser';
import { Observable, Observer } from 'rxjs';

@Component({
  selector: 'app-google-maps',
  templateUrl: './google-maps.component.html',
  styleUrls: ['./google-maps.component.css']
})

@Injectable()
export class GoogleMapsComponent implements OnInit {
  constructor(private http: HttpClient, private sanitizer: DomSanitizer, yearsInTheFutureElement: ElementRef) {
    this.yearsInTheFutureElement = yearsInTheFutureElement;
  }

  KEY = 'AIzaSyBjPxuX_hTYnBTDK-r3AETyOf8VRInmXJA';
  MAP_TYPE = 'satellite';
  SCALE = 1;
  SIZE = '448x473'; // 2 x 2 patches of 224
  zoom = 12;
  center!: google.maps.LatLngLiteral;
  mapOptions: google.maps.MapOptions = {
    center: { lat: 40.714728, lng: -73.998672 },
    streetViewControl: false,
    fullscreenControl: false,
  };
  @ViewChild(GoogleMap, { static: false }) map!: GoogleMap;
  @ViewChild('yearsInTheFuture', { static: true }) yearsInTheFutureElement: ElementRef;
  yearsInTheFuture!: number;
  response!: string;
  image!: any;
  price!: number;


  ngOnInit() {
    this.yearsInTheFuture = 0;
  }

  getBase64ImageFromURL(url: string) {
    return Observable.create((observer: Observer<string>) => {
      // create an image object
      let img = new Image();
      img.crossOrigin = 'Anonymous';
      img.src = url;
      if (!img.complete) {
          // This will call another method that will create image from url
          img.onload = () => {
          observer.next(this.getBase64Image(img));
          observer.complete();
        };       img.onerror = (err) => {
           observer.error(err);
        };     } else {
          observer.next(this.getBase64Image(img));
          observer.complete();
      }
    });
 }


  getBase64Image(img: HTMLImageElement) {
    var canvas = document.createElement("canvas");
    canvas.width = img.width;
    canvas.height = img.height;
    var ctx = canvas.getContext("2d");
    ctx!.drawImage(img, 0, 0);
    var dataURL = canvas.toDataURL("image/png");
    return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
  }

  getMapImage() {
    this.getBase64ImageFromURL(this.getRequest()).subscribe((base64data: string) => {
      var jsonResponse = {
            "coordinates": {
              "latitude": 0,
              "longitude": 0
            },
            "image": base64data,
            "yearsInFuture": this.yearsInTheFutureElement.nativeElement.value 
      };

      this.http.post<JSON>('http://localhost:8080/api/predictDevelopment', jsonResponse).subscribe({
        next: data => {
          const response = JSON.parse(JSON.stringify(data));
          const b64Image = response.image;
          const predictedPrice = response.predictedPrice;
          this.image = this.sanitizeImage(b64Image);
          this.price = predictedPrice;
        },
        error: error => {
            console.error('There was an error!', error);
        }
      });
    }
  )}
  
  sanitizeImage(image: string) {
    return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + image);
  }
  

  getRequest() {
    let currentCenter = JSON.stringify(this.map.getCenter());
    let coords = JSON.parse(currentCenter);
    let request = 'https://maps.googleapis.com/maps/api/staticmap?' +
      'center=' + coords.lat + ',' + coords.lng +
      '&zoom=' + this.map.getZoom() +
      '&size=' + this.SIZE +
      '&scale=' + this.SCALE +
      '&maptype=' + this.MAP_TYPE +
      '&key= ' + this.KEY;
    return request;
  }
}
