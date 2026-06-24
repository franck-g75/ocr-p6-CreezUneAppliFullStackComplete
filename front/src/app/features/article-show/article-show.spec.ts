import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleShow } from './article-show';

describe('ArticleShow', () => {
  let component: ArticleShow;
  let fixture: ComponentFixture<ArticleShow>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleShow]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArticleShow);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
