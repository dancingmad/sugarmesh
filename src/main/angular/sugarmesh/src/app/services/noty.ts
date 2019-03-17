export class Noty {
  message: string;
  type: NotyType;
  cssClass: string;

  constructor(message: string, type:NotyType) {
    this.message = message;
    this.type = type;

    switch (type) {
      case NotyType.ERROR: this.cssClass = 'alert-danger'; break;
      case NotyType.INFO: this.cssClass = 'alert-success'; break;
      case NotyType.WARNING: this.cssClass = 'alert-warning'; break;
    }

  }

}

export enum NotyType {
  ERROR,
  INFO,
  WARNING
}
