import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  Renderer2,
  ViewChild,
  ViewEncapsulation,
} from '@angular/core';
import { CartComponent } from '../cart/cart.component';
import { ActivatedRoute, Router } from '@angular/router';
import { AppRoutingModule } from '../../app.routes';
import { CartService } from '../../service/cart.service';
import { HomeComponent } from '../home/home.component';
import { UserResponse } from '../../responses/userResponse';
import { UserService } from '../../service/user.service';
import { NgbPopoverConfig } from '@ng-bootstrap/ng-bootstrap';
import { bottom } from '@popperjs/core';
import { TokenService } from '../../service/token.service';
import { CommmentService } from '../../service/comment.service';
import { initFlowbite } from 'flowbite';
import { AuthService } from 'src/app/service/auth.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit, AfterViewInit {
  userResponse!: UserResponse | undefined;
  isPoperoverOpen = false;
  totalQuantity: number = 0;
  isLoggedIn: boolean = false;
  flag = true;
  keyword: string = '';
  constructor(
    private userService: UserService,
    private router: Router,
    private cartService: CartService,
    private authService: AuthService,
    private renderer: Renderer2
  ) {
    this.totalQuantity = cartService.getCartSize1().size;
  }
  ngAfterViewInit(): void {
    this.userResponse = this.userService.getUserResponseFromLocalStorage()!;
  }

  ngOnInit(): void {
    this.userResponse = this.userService.getUserResponseFromLocalStorage()!;
    this.authService.isLoggedIn.subscribe((data) => {
      this.isLoggedIn = data;
      this.reloadHeader();
    });
    initFlowbite();
    this.navigateItem(localStorage.getItem('itemNav')!);
  }
  getUserProfile() {
    if (this.flag) {
      this.userResponse = this.userService.getUserResponseFromLocalStorage()!;
      initFlowbite();
    }
    this.flag = false;
  }
  protected readonly bottom = bottom;
  togglePopover(event: Event) {
    event.preventDefault();
    this.isPoperoverOpen = !this.isPoperoverOpen;
  }
  handleItemClick(index: number) {
    if (index === 0) {
      this.router.navigate(['/account']);
    } else {
      if (index === 2) {
        this.authService.logout();
      } else {
        if (index === 1) {
          this.router.navigate(['/orders']);
        }
      }
    }
    this.isPoperoverOpen = false;
  }
  navigateItem(itemNav: string) {
    localStorage.setItem('itemNav', itemNav);
    const item = localStorage.getItem('itemNav');
    const li = document.querySelectorAll('.itemNav');
    li.forEach((element) => {
      if (element.textContent === item) {
        element.classList.add('md:text-blue-700');
      } else {
        element.classList.remove('md:text-blue-700');
      }
    });
  }
  reloadHeader() {
    this.userResponse = this.userService.getUserResponseFromLocalStorage()!;
  }
  isHovered1 = false;
  isHovered2 = false;
  isHovered3 = false;
  isHovered4 = false;

  hoverItemMenu(item: number, isHovered: boolean) {
    if (item === 1) {
      this.isHovered1 = isHovered;
    }
    if (item === 2) {
      this.isHovered2 = isHovered;
    }
    if (item === 3) {
      this.isHovered3 = isHovered;
    }
    if (item === 4) {
      this.isHovered4 = isHovered;
    }
  }

  searchProduct(value: string) {
    this.router
      .navigate(['/products'], { queryParams: { keyword: value } })
      .then(() => {
        this.renderer.setProperty(
          window.location,
          'href',
          window.location.href
        );
      });
  }
  filterCategory(categoryName: string) {
    this.router
      .navigate(['/products'], {
        queryParams: { categoryName: categoryName, filter: true },
      })
      .then(() => {
        this.renderer.setProperty(
          window.location,
          'href',
          window.location.href
        );
      });
  }
}
