import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleHome } from './article-home';

describe('ArticleHome', () => {
  let component: ArticleHome;
  let fixture: ComponentFixture<ArticleHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArticleHome);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
