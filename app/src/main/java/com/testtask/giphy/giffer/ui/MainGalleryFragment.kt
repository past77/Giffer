package com.testtask.giphy.giffer.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.testtask.giphy.giffer.R
import com.testtask.giphy.giffer.data.models.ImageData
import com.testtask.giphy.giffer.databinding.ActivityMainBinding.inflate
import com.testtask.giphy.giffer.databinding.FragmentMainGalleryBinding
import com.testtask.giphy.giffer.viewmodels.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainGalleryFragment : Fragment(), GifAdapter.OnItemClickListener {

    private lateinit var binding: FragmentMainGalleryBinding
    private val viewModel: GalleryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainGalleryBinding.inflate(layoutInflater)
        return binding.root
//        return inflater.inflate(R.layout.fragment_main_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("R# - onViewCreated in  GalleryFragment")
        super.onViewCreated(view, savedInstanceState)

        println("R# - onViewCreated in  GalleryFragment")
        val adapter = GifAdapter(this)
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = GifLoadStateAdapter { adapter.retry() },
                footer = GifLoadStateAdapter { adapter.retry() }
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }

        }

        viewModel.gifs.observe(viewLifecycleOwner) {
            println("R$ - observe")
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                }
                else {
                    textViewEmpty.isVisible = false
                }
            }
        }

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.deleteGif(adapter.getGifId(viewHolder.adapterPosition))
                    Toast.makeText(
                        context,
                       "R.string.note_deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        setHasOptionsMenu(true)
    }

    override fun onItemClick(image: ImageData) {
        findNavController().navigate(MainGalleryFragmentDirections.actionMainGalleryFragmentToGifDetailsFragment(image))
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.serach_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                println("R# query - $query")
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchGifs(query)
                    searchView.clearFocus() // close keyboard
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}