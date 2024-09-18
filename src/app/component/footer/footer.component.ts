import { Component } from '@angular/core';
import { CartComponent } from '../cart/cart.component';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent {
  toogle() {
    console.log('toogle');
  }
}
