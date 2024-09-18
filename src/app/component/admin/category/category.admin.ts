import { Component, ViewChild } from '@angular/core';
import { CategoryService } from '../../../service/category.service';
import { Category } from '../../../models/category';
import { CategoryDTO } from '../../../dtos/categoryDto';
import { ActivatedRoute, Router } from '@angular/router';
import { initFlowbite } from 'flowbite';

@Component({
  selector: 'app-category-admin',
  templateUrl: './category.admin.html',
  styleUrls: ['./category.admin.scss'],
})
export class CategoryAdminComponent {
  categories: Category[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 12;
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  selectedAction: string = '';
  onModal = false;
  category!: CategoryDTO;
  categoryId: number = 0;
  categoryName: string = '';
  constructor(
    private categoryService: CategoryService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    initFlowbite();
    debugger;
    window.scrollTo(0, 0);
    this.getAllCategories(this.keyword, this.currentPage, this.itemsPerPage);
  }

  getAllCategories(keyword: string, page: number, limit: number) {
    debugger;
    this.categoryService.getAllCategories(keyword, page, limit).subscribe({
      next: (response: any) => {
        debugger;
        this.categories = response.categories;
        this.totalPages = response.totalPage;
        this.visiblePages = this.generateVisiblePages(
          this.currentPage,
          this.totalPages
        );
        debugger;
      },
      complete: () => {
        debugger;
      },
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }

  generateVisiblePages(currentPage: number, totalPages: number): number[] {
    const maxVisiblePage = 5;
    const haftVisiblePage = Math.floor(maxVisiblePage / 2);
    let startPage = Math.max(currentPage - haftVisiblePage, 1);
    let endPage = Math.min(startPage + maxVisiblePage - 1, totalPages);
    if (endPage - startPage + 1 < maxVisiblePage) {
      startPage = Math.max(endPage - maxVisiblePage + 1, 1);
    }
    return new Array(endPage - startPage + 1)
      .fill(0)
      .map((_, index) => startPage + index);
  }
  onPageChange(page: number) {
    debugger;
    window.scrollTo(0, 0);
    this.currentPage = page;
    this.getAllCategories(
      this.keyword,
      this.currentPage - 1,
      this.itemsPerPage
    );
    localStorage.setItem('currentCategoryAdminPage', String(this.currentPage));
  }

  CreateCategory(categoryDto: CategoryDTO) {
    debugger;
    categoryDto = {
      category_name: this.categoryName,
    };
    this.categoryService.createCategory(categoryDto).subscribe({
      next: (response: any) => {
        alert(response.message);
        this.categories.push(response.category);
        debugger;
      },
      complete: () => {
        debugger;
      },
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }

  UpdateCategory(categoryDto: CategoryDTO, categorId: number) {
    debugger;
    categoryDto = {
      category_name: this.categoryName,
    };
    this.categoryService.updateCategory(categorId, categoryDto).subscribe({
      next: (response: any) => {
        alert(response.message);
        debugger;
      },
      complete: () => {
        debugger;
        this.getAllCategories(
          this.keyword,
          this.currentPage,
          this.itemsPerPage
        );
      },
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }
  onRemove(categoryId: number) {
    debugger;
    this.categoryService.deleteCategory(categoryId).subscribe({
      next: (response) => {
        alert(response.message);
        this.getAllCategories(
          this.keyword,
          this.currentPage,
          this.itemsPerPage
        );
        // this.router.navigate(['../'], { relativeTo: this.route });
        debugger;
      },
      complete: () => {
        debugger;
      },
      error: (error) => {
        debugger;
        console.log(error);
      },
    });
  }
  searchCategory() {
    this.getAllCategories(this.keyword, this.currentPage, this.itemsPerPage);
  }
  dropdownToggle() {
    const dropdown = document.getElementById('dropdown');
    dropdown?.classList.toggle('show');
  }
}
